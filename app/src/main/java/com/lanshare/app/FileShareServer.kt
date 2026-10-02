package com.lanshare.app

import android.content.Context
import fi.iki.elonen.NanoHTTPD
import java.io.File
import java.io.FileInputStream

class FileShareServer(
    private val ctx: Context,
    private val rootDir: File,
    port: Int,
    private val onChanged: () -> Unit
) : NanoHTTPD(port) {

    private val page by lazy { PAGE_HTML }

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri ?: "/"
        val params = session.parameters
        return when {
            uri == "/" -> html(page)
            uri == "/api/files" -> json(listJson())
            uri == "/api/wallpapers" -> wpList()
            uri.startsWith("/wallpaper/") -> wpFile(uri.substringAfter("/wallpaper/"))
            uri == "/api/upload" && session.method == Method.POST -> upload(session)
            uri == "/api/delete" && session.method == Method.POST -> del(params)
            uri == "/f" -> file(params)
            else -> newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "404")
        }
    }

    private fun html(s: String) = newFixedLengthResponse(Response.Status.OK, "text/html; charset=utf-8", s)
    private fun json(s: String) = newFixedLengthResponse(Response.Status.OK, "application/json; charset=utf-8", s)

    private fun wpList(): Response = try {
        json(ctx.assets.open("wallpapers.json").bufferedReader().use { it.readText() })
    } catch (_: Exception) { json("""{"count":0,"items":[]}""") }

    private fun wpFile(name: String): Response {
        val safe = name.replace("..", "").replace("/", "")
        return try {
            val input = ctx.assets.open("wallpapers/" + safe)
            val mime = when {
                safe.endsWith(".png", true) -> "image/png"
                safe.endsWith(".webp", true) -> "image/webp"
                safe.endsWith(".gif", true) -> "image/gif"
                else -> "image/jpeg"
            }
            newChunkedResponse(Response.Status.OK, mime, input).apply {
                addHeader("Cache-Control", "public, max-age=86400")
            }
        } catch (_: Exception) {
            newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "not found")
        }
    }

    private fun listJson(): String {
        val files = rootDir.listFiles()?.filter { !it.name.startsWith(".") }?.map {
            """{"name":"${it.name.replace("\"", "\\")}","size":${it.length()},"mtime":${it.lastModified()}}"""
        } ?: emptyList()
        return "[${files.joinToString(",")}]"
    }

    private fun upload(session: IHTTPSession): Response {
        val raw = session.parameters["name"]?.firstOrNull() ?: return jerr("no name")
        val safe = safeName(raw) ?: return jerr("bad name")
        val tmp = File(rootDir, ".up-" + System.nanoTime())
        return try {
            session.inputStream.use { i -> tmp.outputStream().use { i.copyTo(it) } }
            val tgt = unique(safe)
            tmp.renameTo(tgt)
            onChanged()
            json("""{"ok":true,"name":"${tgt.name}"}""")
        } catch (e: Exception) {
            tmp.delete()
            jerr("upload failed")
        }
    }

    private fun del(params: Map<String, List<String>>): Response {
        val name = params["name"]?.firstOrNull() ?: return jerr("no name")
        val safe = safeName(name) ?: return jerr("bad name")
        val f = File(rootDir, safe)
        return if (f.exists() && f.delete()) { onChanged(); json("""{"ok":true}""") } else jerr("fail")
    }

    private fun file(params: Map<String, List<String>>): Response {
        val name = params["name"]?.firstOrNull() ?: return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/plain", "no name")
        val safe = safeName(name) ?: return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/plain", "bad")
        val f = File(rootDir, safe)
        if (!f.exists() || !f.isFile) return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "404")
        val mime = mime(f.name)
        val resp = newChunkedResponse(Response.Status.OK, mime, FileInputStream(f))
        if (params["dl"]?.firstOrNull() == "1")
            resp.addHeader("Content-Disposition", "attachment; filename*=UTF-8''" + enc(f.name))
        return resp
    }

    private fun safeName(raw: String): String? {
        val n = raw.replace("\\", "/").substringAfterLast("/").trim().trim('.')
        if (n.isEmpty() || n == "." || n == "..") return null
        return if (n.toByteArray().size > 200) n.take(120) else n
    }

    private fun unique(name: String): File {
        val f = File(rootDir, name)
        if (!f.exists()) return f
        val dot = name.lastIndexOf('.')
        val stem = if (dot > 0) name.substring(0, dot) else name
        val ext = if (dot > 0) name.substring(dot) else ""
        var i = 1
        while (true) {
            val t = File(rootDir, stem + "(" + i + ")" + ext)
            if (!t.exists()) return t
            i++
        }
    }

    private fun jerr(m: String) = newFixedLengthResponse(Response.Status.BAD_REQUEST, "application/json", """{"ok":false,"error":"$m"}""")
    private fun enc(s: String) = java.net.URLEncoder.encode(s, "UTF-8").replace("+", "%20")
    private fun mime(n: String): String = when (n.substringAfterLast('.', "").lowercase()) {
        "html","htm" -> "text/html; charset=utf-8"
        "css" -> "text/css; charset=utf-8"
        "js" -> "application/javascript; charset=utf-8"
        "json" -> "application/json; charset=utf-8"
        "png" -> "image/png"
        "jpg","jpeg" -> "image/jpeg"
        "gif" -> "image/gif"
        "webp" -> "image/webp"
        "svg" -> "image/svg+xml"
        "mp4" -> "video/mp4"
        "mp3" -> "audio/mpeg"
        "pdf" -> "application/pdf"
        "zip" -> "application/zip"
        "txt","md","log" -> "text/plain; charset=utf-8"
        else -> "application/octet-stream"
    }
}
