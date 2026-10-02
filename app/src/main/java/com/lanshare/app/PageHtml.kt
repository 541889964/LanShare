package com.lanshare.app

internal const val PAGE_HTML = """
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1,viewport-fit=cover">
<title>LanShare</title>
<style>
*{box-sizing:border-box;-webkit-tap-highlight-color:transparent}
:root{--bg:#f7f8fc;--panel:rgba(255,255,255,.92);--text:#161a23;--muted:#6b7280;
--border:#e4e6ef;--accent:#4f46e5;--accent2:#7c73ff;--soft:#eef0ff;
--danger:#e11d48;--ok:#16a34a;--radius:16px;
--shadow:0 1px 2px rgba(16,24,40,.05),0 8px 24px rgba(16,24,40,.06)}
@media(prefers-color-scheme:dark){:root{--bg:#0e1014;--panel:rgba(23,26,33,.92);
--text:#e9ebf1;--muted:#9aa1b1;--border:#252932;--accent:#7c73ff;--accent2:#a79dff;
--soft:#22223a;--danger:#ff5c7c;--ok:#4ade80;
--shadow:0 1px 2px rgba(0,0,0,.4),0 8px 24px rgba(0,0,0,.35)}}
html,body{margin:0;padding:0;min-height:100vh}
body{background:var(--bg);color:var(--text);padding:0 16px 72px;
font:15px/1.55 -apple-system,BlinkMacSystemFont,"PingFang SC","Microsoft YaHei",sans-serif;
-webkit-font-smoothing:antialiased;position:relative}
.bg-layer{position:fixed;inset:0;z-index:-2;background-size:cover;
background-position:center;background-repeat:no-repeat;
opacity:.32;filter:saturate(1.05)}
.bg-mask{position:fixed;inset:0;z-index:-1;
background:linear-gradient(180deg,rgba(247,248,252,.72),rgba(247,248,252,.94))}
@media(prefers-color-scheme:dark){.bg-mask{
background:linear-gradient(180deg,rgba(14,16,20,.75),rgba(14,16,20,.95))}}
.wrap{max-width:960px;margin:0 auto;position:relative;z-index:1}
header{display:flex;align-items:center;justify-content:space-between;gap:12px;
padding:24px 0 18px;flex-wrap:wrap;animation:sd .5s cubic-bezier(.16,1,.3,1) both}
@keyframes sd{from{opacity:0;transform:translateY(-12px)}to{opacity:1;transform:translateY(0)}}
.brand{display:flex;align-items:center;gap:12px;min-width:0}
.logo{width:48px;height:48px;flex:none;border-radius:14px;
background:linear-gradient(135deg,var(--accent),var(--accent2));
display:grid;place-items:center;font-size:22px;color:white;
box-shadow:0 8px 22px rgba(79,70,229,.32)}
h1{font-size:18px;margin:0;font-weight:700}
.sub{margin:3px 0 0;font-size:12px;color:var(--muted);
font-family:ui-monospace,Menlo,monospace;overflow:hidden;text-overflow:ellipsis;
white-space:nowrap;max-width:60vw}
.acts{display:flex;gap:8px}
.btn{display:inline-flex;align-items:center;gap:6px;border:1px solid var(--border);
background:var(--panel);color:var(--text);padding:8px 14px;border-radius:11px;
font:inherit;font-size:13.5px;cursor:pointer;text-decoration:none;
transition:all .22s cubic-bezier(.16,1,.3,1);white-space:nowrap;line-height:1.3;
backdrop-filter:blur(12px);-webkit-backdrop-filter:blur(12px)}
.btn:hover{background:var(--soft);border-color:var(--accent);color:var(--accent);
transform:translateY(-1px)}
.btn:active{transform:translateY(0) scale(.97)}
.btn.danger:hover{background:rgba(225,29,72,.08);border-color:var(--danger);
color:var(--danger)}
.qr-card{display:flex;gap:20px;align-items:center;background:var(--panel);
border:1px solid var(--border);border-radius:var(--radius);padding:16px 18px;
box-shadow:var(--shadow);margin-bottom:22px;position:relative;overflow:hidden;
animation:fu .55s cubic-bezier(.16,1,.3,1) .05s both;
backdrop-filter:blur(16px);-webkit-backdrop-filter:blur(16px)}
@keyframes fu{from{opacity:0;transform:translateY(16px)}to{opacity:1;transform:translateY(0)}}
.qr-card::before{content:"";position:absolute;top:0;left:0;right:0;height:3px;
background:linear-gradient(90deg,var(--accent),var(--accent2),var(--accent));opacity:.9}
.qr-box{width:150px;height:150px;flex:none;background:#fff;border-radius:14px;
padding:8px;box-sizing:content-box;display:grid;place-items:center;overflow:hidden;
cursor:zoom-in;border:2px solid var(--accent);
box-shadow:0 6px 18px rgba(79,70,229,.2);
transition:transform .3s cubic-bezier(.16,1,.3,1)}
.qr-box:hover{transform:scale(1.03)}
.qr-box:active{transform:scale(.96)}
.qr-box svg{display:block;width:100%;height:100%}
.qr-meta{min-width:0;flex:1}
.qr-title{font-weight:700;font-size:15px;color:var(--accent)}
.qr-url{font-family:ui-monospace,Menlo,monospace;font-size:14px;color:var(--accent);
margin-top:8px;word-break:break-all;font-weight:600}
.qr-hint{font-size:12.5px;color:var(--muted);margin-top:8px;line-height:1.7}
.qr-hint b{color:var(--text)}
.drop{border:2px dashed var(--border);border-radius:20px;background:var(--panel);
padding:34px 20px;text-align:center;transition:all .3s cubic-bezier(.16,1,.3,1);
box-shadow:var(--shadow);cursor:pointer;user-select:none;
animation:fu .55s cubic-bezier(.16,1,.3,1) .1s both;
backdrop-filter:blur(12px);-webkit-backdrop-filter:blur(12px)}
.drop:hover{border-color:var(--accent2);transform:translateY(-2px)}
.drop.over{border-color:var(--accent);background:var(--soft);transform:scale(1.01)}
.drop .big{font-size:38px;line-height:1;display:inline-block;
animation:fl 3s ease-in-out infinite}
@keyframes fl{0%,100%{transform:translateY(0)}50%{transform:translateY(-6px)}}
.drop .t{font-size:16px;font-weight:600;margin-top:12px}
.drop .h{font-size:12.5px;color:var(--muted);margin-top:7px}
.link{background:none;border:0;color:var(--accent);font:inherit;font-weight:700;
cursor:pointer;padding:0;text-decoration:underline;text-underline-offset:3px}
.transfers{margin-top:14px;display:grid;gap:10px}
.transfer{background:var(--panel);border:1px solid var(--border);border-radius:13px;
padding:11px 14px;box-shadow:var(--shadow);
animation:si .35s cubic-bezier(.16,1,.3,1) both;
backdrop-filter:blur(12px);-webkit-backdrop-filter:blur(12px)}
@keyframes si{from{opacity:0;transform:translateY(-8px) scale(.98)}
to{opacity:1;transform:translateY(0) scale(1)}}
.transfer .thead{display:flex;justify-content:space-between;gap:12px;font-size:13.5px}
.transfer .tname{font-weight:600;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}
.transfer .status{color:var(--muted);flex:none;font-size:12.5px;
font-family:ui-monospace,Menlo,monospace}
.bar{height:6px;background:var(--soft);border-radius:99px;overflow:hidden;margin-top:9px}
.bar>i{display:block;height:100%;width:0;border-radius:99px;
background:linear-gradient(90deg,var(--accent),var(--accent2));
transition:width .25s cubic-bezier(.16,1,.3,1);
box-shadow:0 0 8px rgba(79,70,229,.5)}
.transfer.done .tname{color:var(--ok)}
.transfer.err .status{color:var(--danger)}
.files{margin-top:26px}
.files-head{display:flex;align-items:center;justify-content:space-between;gap:12px;
margin-bottom:12px;flex-wrap:wrap;animation:fu .5s cubic-bezier(.16,1,.3,1) .15s both}
.files-head h2{font-size:16px;margin:0;display:flex;align-items:center;gap:8px}
.count{font-size:12px;font-weight:700;color:var(--accent);background:var(--soft);
padding:2px 10px;border-radius:99px}
.search{border:1px solid var(--border);background:var(--panel);color:var(--text);
border-radius:11px;padding:8px 13px;font:inherit;font-size:13.5px;width:220px;
max-width:55vw;outline:none;transition:all .22s cubic-bezier(.16,1,.3,1);
backdrop-filter:blur(12px);-webkit-backdrop-filter:blur(12px)}
.search:focus{border-color:var(--accent);box-shadow:0 0 0 3px var(--soft)}
.list{display:grid;gap:10px}
.file{display:flex;align-items:center;gap:14px;background:var(--panel);
border:1px solid var(--border);border-radius:var(--radius);padding:12px 14px;
box-shadow:var(--shadow);transition:all .25s cubic-bezier(.16,1,.3,1);
animation:fi .4s cubic-bezier(.16,1,.3,1) both;
backdrop-filter:blur(12px);-webkit-backdrop-filter:blur(12px)}
@keyframes fi{from{opacity:0;transform:translateY(8px)}to{opacity:1;transform:translateY(0)}}
.file:hover{border-color:var(--accent2);transform:translateY(-2px);
box-shadow:0 4px 8px rgba(16,24,40,.06),0 16px 40px rgba(16,24,40,.1)}
.file .ico{width:46px;height:46px;flex:none;display:grid;place-items:center;
font-size:22px;background:var(--soft);border-radius:13px}
.file .meta{min-width:0;flex:1}
.file .name{font-weight:600;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}
.file .s{font-size:12.5px;color:var(--muted);margin-top:3px;
font-family:ui-monospace,Menlo,monospace}
.file .ops{display:flex;gap:8px;flex:none}
.empty{text-align:center;color:var(--muted);padding:52px 16px;font-size:14px;
animation:fu .5s cubic-bezier(.16,1,.3,1) both}
.toast{position:fixed;left:50%;bottom:28px;
transform:translate(-50%,24px) scale(.9);background:rgba(17,24,39,.94);color:#fff;
padding:11px 22px;border-radius:99px;font-size:13.5px;opacity:0;pointer-events:none;
transition:all .35s cubic-bezier(.16,1,.3,1);
box-shadow:0 12px 32px rgba(0,0,0,.3);z-index:99;max-width:80vw;text-align:center;
backdrop-filter:blur(12px);-webkit-backdrop-filter:blur(12px)}
.toast.show{opacity:1;transform:translate(-50%,0) scale(1)}
.qr-modal{position:fixed;inset:0;background:rgba(17,24,39,.78);display:none;
place-items:center;z-index:200;padding:20px;opacity:0;transition:opacity .3s ease;
backdrop-filter:blur(16px);-webkit-backdrop-filter:blur(16px)}
.qr-modal.on{display:grid;opacity:1}
.qr-modal .inner{background:#fff;border-radius:22px;padding:24px;
max-width:min(92vw,440px);box-shadow:0 30px 80px rgba(0,0,0,.5);
text-align:center;border:3px solid var(--accent);
transform:scale(.85);transition:transform .4s cubic-bezier(.16,1,.3,1)}
.qr-modal.on .inner{transform:scale(1)}
.qr-modal .inner svg{width:100%;height:auto;display:block}
.qr-modal .cap{margin-top:14px;font-size:13px;color:#374151;
font-family:ui-monospace,Menlo,monospace;word-break:break-all}
@media(max-width:560px){
.file{flex-wrap:wrap}.file .ops{width:100%;justify-content:flex-end}
.file .ops .btn{flex:1;justify-content:center}
.search{width:100%;max-width:none}
.files-head{flex-direction:column;align-items:stretch}
.sub{max-width:70vw}
.qr-card{flex-direction:column;text-align:center;gap:14px}
.qr-box{width:180px;height:180px}}
</style>
</head>
<body>
<div class="bg-layer" id="bgLayer"></div>
<div class="bg-mask"></div>
<div class="wrap">
<header>
<div class="brand">
<div class="logo">📡</div>
<div style="min-width:0">
<h1>LanShare</h1>
<p class="sub" id="addrTop">连接中…</p>
</div>
</div>
<div class="acts">
<button class="btn" id="copyBtn" type="button">🔗 复制</button>
<button class="btn" id="refreshBtn" type="button">↻ 刷新</button>
</div>
</header>
<section class="qr-card">
<div class="qr-box" id="qrBox"></div>
<div class="qr-meta">
<div class="qr-title">📱 手机扫码直接打开</div>
<div class="qr-url" id="addr">连接中…</div>
<div class="qr-hint">用 <b>Via、夸克、Chrome</b> 扫码即可进入，<br>同一 Wi-Fi 下的设备都能访问。</div>
</div>
</section>
<section class="drop" id="drop">
<div class="big">⬆️</div>
<div class="t">拖拽文件到这里，或 <button class="link" id="pickBtn" type="button">点击选择</button></div>
<div class="h">支持多文件 · 支持 Ctrl/⌘+V 粘贴</div>
<input type="file" id="fileInput" multiple hidden>
</section>
<section class="transfers" id="transfers" hidden></section>
<section class="files">
<div class="files-head">
<h2>文件 <span class="count" id="count">0</span></h2>
<input class="search" id="search" type="search" placeholder="搜索文件名…">
</div>
<div class="list" id="list"></div>
<div class="empty" id="empty" hidden>还没有文件，上传第一个吧 🎉</div>
</section>
</div>
<div class="toast" id="toast"></div>
<div class="qr-modal" id="qrModal"><div class="inner"><div id="qrBig"></div><div class="cap" id="qrCap"></div></div></div>
<script>
var QR=(function(){
var EXP=new Array(512),LOG=new Array(256);
(function(){var x=1;for(var i=0;i<255;i++){EXP[i]=x;LOG[x]=i;x<<=1;if(x&256)x^=285}
for(var i=255;i<512;i++)EXP[i]=EXP[i-255]})();
function mul(a,b){return(a===0||b===0)?0:EXP[LOG[a]+LOG[b]]}
function genPoly(n){var g=[1];for(var i=0;i<n;i++){var ng=[];
for(var k=0;k<=g.length;k++)ng.push(0);
for(var j=0;j<g.length;j++){ng[j]^=mul(g[j],EXP[i]);ng[j+1]^=g[j]}g=ng}
return g.reverse()}
function rs(data,ecLen){var g=genPoly(ecLen),res=data.slice();
for(var i=0;i<ecLen;i++)res.push(0);
for(var i=0;i<data.length;i++){var c=res[i];if(c===0)continue;
for(var j=0;j<g.length;j++)res[i+j]^=mul(g[j],c)}
return res.slice(data.length)}
var VER=[[19,26,7,1],[34,44,10,1],[55,70,15,1],[80,100,20,1],[108,134,26,1],
[136,172,18,2],[156,196,20,2],[194,242,24,2],[232,292,30,2]];
var ALIGN=[[],[6,18],[6,22],[6,26],[6,30],[6,34],[6,22,38],[6,24,42],[6,26,46]];
function utf8(s){var o=[];for(var i=0;i<s.length;i++){var c=s.charCodeAt(i);
if(c<0x80)o.push(c);
else if(c<0x800){o.push(0xC0|(c>>6));o.push(0x80|(c&63))}
else if(c<0xD800||c>=0xE000){o.push(0xE0|(c>>12));o.push(0x80|((c>>6)&63));o.push(0x80|(c&63))}
else{i++;var cp=0x10000+(((c&0x3FF)<<10)|(s.charCodeAt(i)&0x3FF));
o.push(0xF0|(cp>>18));o.push(0x80|((cp>>12)&63));o.push(0x80|((cp>>6)&63));o.push(0x80|(cp&63))}}
return o}
function bch15(d){var x=d<<10;for(var i=14;i>=10;i--)if(x&(1<<i))x^=0x537<<(i-10);return((d<<10)|x)^0x5412}
function bch18(v){var d=v<<12;for(var i=17;i>=12;i--)if(d&(1<<i))d^=0x1F25<<(i-12);return(v<<12)|d}
function buildMatrix(ver,cw){
var size=ver*4+17,m=[],res=[];
for(var i=0;i<size;i++){var r=[],rr=[];for(var j=0;j<size;j++){r.push(0);rr.push(false)}m.push(r);res.push(rr)}
function finder(row,col){for(var r=-1;r<=7;r++)for(var c=-1;c<=7;c++){
var rr=row+r,cc=col+c;if(rr<0||rr>=size||cc<0||cc>=size)continue;
var v=0;if(r>=0&&r<=6&&c>=0&&c<=6)v=(r===0||r===6||c===0||c===6||(r>=2&&r<=4&&c>=2&&c<=4))?1:0;
m[rr][cc]=v;res[rr][cc]=true}}
finder(0,0);finder(0,size-7);finder(size-7,0);
var ap=ALIGN[ver-1];
for(var i=0;i<ap.length;i++)for(var j=0;j<ap.length;j++){
var r=ap[i],c=ap[j];if(res[r][c])continue;
for(var dr=-2;dr<=2;dr++)for(var dc=-2;dc<=2;dc++){
m[r+dr][c+dc]=(Math.abs(dr)===2||Math.abs(dc)===2||(dr===0&&dc===0))?1:0;
res[r+dr][c+dc]=true}}
for(var i=8;i<size-8;i++){
if(!res[6][i]){m[6][i]=(i%2===0)?1:0;res[6][i]=true}
if(!res[i][6]){m[i][6]=(i%2===0)?1:0;res[i][6]=true}}
m[size-8][8]=1;res[size-8][8]=true;
for(var i=0;i<9;i++){res[8][i]=true;res[i][8]=true}
for(var i=0;i<8;i++){res[8][size-1-i]=true;res[size-1-i][8]=true}
if(ver>=7){for(var i=0;i<6;i++)for(var j=0;j<3;j++){
res[size-11+j][i]=true;res[i][size-11+j]=true}}
var bitIdx=0,total=cw.length*8,col=size-1,up=true;
while(col>0){if(col===6)col--;
for(var i=0;i<size;i++){var row=up?size-1-i:i;
for(var c=0;c<2;c++){var cc=col-c;if(res[row][cc])continue;
var bit=0;if(bitIdx<total){bit=(cw[bitIdx>>3]>>(7-(bitIdx&7)))&1;bitIdx++}
m[row][cc]=bit}}
up=!up;col-=2}
for(var r=0;r<size;r++)for(var c=0;c<size;c++)
if(!res[r][c]&&(r+c)%2===0)m[r][c]^=1;
var fmt=bch15((1<<3)|0);
for(var i=0;i<15;i++){var bit=(fmt>>i)&1;
if(i<6)m[i][8]=bit;else if(i<8)m[i+1][8]=bit;else m[size-15+i][8]=bit}
for(var i=0;i<15;i++){var bit=(fmt>>i)&1;
if(i<8)m[8][size-i-1]=bit;else if(i<9)m[8][7]=bit;else m[8][14-i]=bit}
if(ver>=7){var vi=bch18(ver);
for(var i=0;i<18;i++){var bit=(vi>>i)&1;
m[Math.floor(i/3)][i%3+size-11]=bit;m[i%3+size-11][Math.floor(i/3)]=bit}}
return m}
function encode(text){
var data=utf8(text),ver=-1;
for(var i=0;i<VER.length;i++)if(data.length<=Math.floor((VER[i][0]*8-12)/8)){ver=i;break}
if(ver<0)return null;
var p=VER[ver],dataCw=p[0],ecPerBlock=p[2],blocks=p[3],bits=[];
function push(val,n){for(var i=n-1;i>=0;i--)bits.push((val>>i)&1)}
push(4,4);push(data.length,8);
for(var i=0;i<data.length;i++)push(data[i],8);
var cap=dataCw*8;push(0,Math.min(4,cap-bits.length));
while(bits.length%8)bits.push(0);
var pads=[0xEC,0x11],pi=0;
while(bits.length<cap)push(pads[pi++%2],8);
var dcw=[];
for(var i=0;i<bits.length;i+=8){var b=0;
for(var j=0;j<8;j++)b=(b<<1)|bits[i+j];dcw.push(b)}
var per=dataCw/blocks,ecb=[];
for(var b=0;b<blocks;b++)ecb.push(rs(dcw.slice(b*per,(b+1)*per),ecPerBlock));
var cw=[];
for(var i=0;i<per;i++)for(var b=0;b<blocks;b++)cw.push(dcw[b*per+i]);
for(var i=0;i<ecPerBlock;i++)for(var b=0;b<blocks;b++)cw.push(ecb[b][i]);
return buildMatrix(ver+1,cw)}
return{encode:encode}})();

(function(){
'use strict';
var $=function(s){return document.querySelector(s)};
var listEl=$('#list'),emptyEl=$('#empty'),countEl=$('#count'),
transfersEl=$('#transfers'),dropEl=$('#drop'),inputEl=$('#fileInput'),
searchEl=$('#search'),addrEl=$('#addr'),addrTopEl=$('#addrTop'),
qrBox=$('#qrBox'),toastEl=$('#toast'),bgLayer=$('#bgLayer'),
modal=$('#qrModal'),modalBig=$('#qrBig'),modalCap=$('#qrCap');
var allFiles=[],query='',currentUrl='';

function el(tag,props){
var n=document.createElement(tag),k,v,i;
if(props)for(k in props){v=props[k];if(v===null||v===undefined)continue;
if(k==='class')n.className=v;else if(k==='text')n.textContent=v;
else if(k.slice(0,2)==='on')n.addEventListener(k.slice(2).toLowerCase(),v);
else n.setAttribute(k,v)}
for(i=2;i<arguments.length;i++)if(arguments[i])n.appendChild(arguments[i]);
return n}

function fmtSize(n){if(n<1024)return n+' B';
var u=['KB','MB','GB','TB'],i=-1;
do{n/=1024;i++}while(n>=1024&&i<u.length-1);
return(n>=100?n.toFixed(0):n.toFixed(1))+' '+u[i]}
function p2(n){return n<10?'0'+n:''+n}
function fmtTime(ms){var d=new Date(ms),diff=(Date.now()-ms)/1000;
if(diff<60)return'刚刚';if(diff<3600)return Math.floor(diff/60)+' 分钟前';
if(diff<86400)return Math.floor(diff/3600)+' 小时前';
if(diff<604800)return Math.floor(diff/86400)+' 天前';
return d.getFullYear()+'-'+p2(d.getMonth()+1)+'-'+p2(d.getDate())}

var EXT={img:['png','jpg','jpeg','gif','webp','svg','bmp','ico'],
vid:['mp4','webm','mov','mkv','avi'],aud:['mp3','wav','ogg','m4a','flac'],
doc:['pdf','doc','docx','xls','xlsx','ppt','pptx','txt','md','csv','json','xml','log'],
zip:['zip','rar','7z','tar','gz']};
var ICONS={img:'🖼️',vid:'🎬',aud:'🎵',doc:'📄',zip:'📦',other:'📎'};
function extOf(n){var i=n.lastIndexOf('.');return i<0?'':n.slice(i+1).toLowerCase()}
function kindOf(n){var e=extOf(n),k;for(k in EXT)if(EXT[k].indexOf(e)>=0)return k;return'other'}
function canPreview(n){var k=kindOf(n),e=extOf(n);
return k==='img'||k==='vid'||k==='aud'||e==='pdf'||e==='txt'||e==='md'}

var toastTimer;
function toast(m){toastEl.textContent=m;toastEl.classList.add('show');
clearTimeout(toastTimer);toastTimer=setTimeout(function(){toastEl.classList.remove('show')},2200)}

function qrSvg(text,px){
var m=QR.encode(text);if(!m)return null;
var n=m.length,q=4,t=n+q*2,d='';
for(var r=0;r<n;r++)for(var c=0;c<n;c++)
if(m[r][c])d+='M'+(c+q)+' '+(r+q)+'h1v1h-1z';
return '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 '+t+' '+t+'" width="'+px+'" height="'+px+'" shape-rendering="crispEdges">'+
'<rect width="'+t+'" height="'+t+'" fill="#ffffff"/><path d="'+d+'" fill="#000000"/></svg>'}
function drawQR(url){var s=qrSvg(url,260);
if(!s){qrBox.textContent='生成失败';return}qrBox.innerHTML=s}
function openBigQR(url){var s=qrSvg(url,640);if(!s)return;
modalBig.innerHTML=s;modalCap.textContent=url;modal.classList.add('on')}
modal.addEventListener('click',function(){modal.classList.remove('on')});
qrBox.addEventListener('click',function(){if(currentUrl)openBigQR(currentUrl)});

function loadWallpaper(){
fetch('/api/wallpapers').then(function(r){return r.json()}).then(function(d){
if(!d||!d.items||!d.items.length)return;
var first=d.items[0];
bgLayer.style.backgroundImage='url(/wallpaper/'+encodeURIComponent(first)+')';
}).catch(function(){})}

function loadInfo(){
var url=location.origin;currentUrl=url;
addrEl.textContent=url;addrTopEl.textContent=url;
drawQR(url)}

var loading=false;
function loadFiles(){if(loading)return;loading=true;
fetch('/api/files',{cache:'no-store'}).then(function(r){return r.json()})
.then(function(d){allFiles=Array.isArray(d)?d:[];render()})
.catch(function(){}).finally(function(){loading=false})}

function render(){
var q=query.trim().toLowerCase();
var shown=q?allFiles.filter(function(f){return f.name.toLowerCase().indexOf(q)>=0}):allFiles;
countEl.textContent=allFiles.length;listEl.innerHTML='';
emptyEl.hidden=shown.length>0;
emptyEl.textContent=q?'没有匹配的文件 🔍':'还没有文件，上传第一个吧 🎉';
for(var i=0;i<shown.length;i++){
var row=fileRow(shown[i]);
row.style.animationDelay=(i*30)+'ms';
listEl.appendChild(row)}}

function fileRow(f){
var ops=el('div',{class:'ops'});
if(canPreview(f.name))ops.appendChild(el('button',{class:'btn',text:'预览',type:'button',
onclick:function(){window.open('/f?name='+encodeURIComponent(f.name),'_blank')}}));
ops.appendChild(el('a',{class:'btn',text:'下载',
href:'/f?name='+encodeURIComponent(f.name)+'&dl=1',download:f.name}));
ops.appendChild(el('button',{class:'btn danger',text:'删除',type:'button',
onclick:function(){removeFile(f)}}));
return el('div',{class:'file'},
el('div',{class:'ico',text:ICONS[kindOf(f.name)]}),
el('div',{class:'meta'},
el('div',{class:'name',text:f.name,title:f.name}),
el('div',{class:'s',text:fmtSize(f.size)+'  ·  '+fmtTime(f.mtime)})),
ops)}

function removeFile(f){
if(!confirm('确定要删除「'+f.name+'」吗？'))return;
fetch('/api/delete?name='+encodeURIComponent(f.name),{method:'POST'})
.then(function(r){return r.json()})
.then(function(d){if(d.ok){toast('已删除');loadFiles()}
else toast(d.error||'删除失败')})
.catch(function(){toast('网络错误')})}

var queue=[],active=0,MAX=3;
function enqueue(list){
var arr=Array.prototype.slice.call(list||[]);if(!arr.length)return;
arr.forEach(function(file){
var row=makeTransferRow(file.name);
transfersEl.hidden=false;transfersEl.appendChild(row);
queue.push({file:file,row:row})});
pump()}
function pump(){while(active<MAX&&queue.length){
var item=queue.shift();active++;
uploadOne(item).finally(function(){active--;pump()})}}
function makeTransferRow(name){
var status=el('span',{class:'status',text:'准备中…'});
var bar=el('div',{class:'bar'},el('i'));
return el('div',{class:'transfer'},
el('div',{class:'thead'},el('span',{class:'tname',text:name}),status),bar)}
function uploadOne(item){
return new Promise(function(resolve){
var file=item.file,row=item.row;
var bar=row.querySelector('i'),status=row.querySelector('.status'),
tname=row.querySelector('.tname');
var xhr=new XMLHttpRequest();
xhr.open('POST','/api/upload?name='+encodeURIComponent(file.name));
xhr.setRequestHeader('Content-Type','application/octet-stream');
xhr.upload.onprogress=function(e){if(!e.lengthComputable)return;
var pct=Math.round(e.loaded/e.total*100);bar.style.width=pct+'%';
status.textContent=fmtSize(e.loaded)+' / '+fmtSize(e.total)+' · '+pct+'%'};
xhr.onload=function(){
if(xhr.status>=200&&xhr.status<300){
bar.style.width='100%';row.classList.add('done');
tname.textContent='✓ '+file.name;status.textContent='完成';
setTimeout(function(){row.style.opacity='0';
row.style.transform='translateY(-6px)';
setTimeout(function(){row.remove();
if(!transfersEl.children.length)transfersEl.hidden=true},300)},1000);
loadFiles()
}else{row.classList.add('err');status.textContent='上传失败'}
resolve()};
xhr.onerror=function(){row.classList.add('err');status.textContent='网络错误';resolve()};
xhr.send(file)})}

$('#pickBtn').addEventListener('click',function(e){e.stopPropagation();inputEl.click()});
dropEl.addEventListener('click',function(){inputEl.click()});
inputEl.addEventListener('change',function(){enqueue(inputEl.files);inputEl.value=''});
var dragDepth=0;
document.addEventListener('dragenter',function(e){e.preventDefault();dragDepth++;
dropEl.classList.add('over')});
document.addEventListener('dragover',function(e){e.preventDefault()});
document.addEventListener('dragleave',function(){dragDepth=Math.max(0,dragDepth-1);
if(!dragDepth)dropEl.classList.remove('over')});
document.addEventListener('drop',function(e){e.preventDefault();dragDepth=0;
dropEl.classList.remove('over');
if(e.dataTransfer&&e.dataTransfer.files&&e.dataTransfer.files.length)
enqueue(e.dataTransfer.files)});
document.addEventListener('paste',function(e){
var items=e.clipboardData&&e.clipboardData.files;
if(items&&items.length){e.preventDefault();enqueue(items)}});
searchEl.addEventListener('input',function(){query=searchEl.value;render()});
$('#refreshBtn').addEventListener('click',function(){loadFiles();toast('已刷新')});
$('#copyBtn').addEventListener('click',function(){
var text=currentUrl||addrEl.textContent;
if(navigator.clipboard&&navigator.clipboard.writeText){
navigator.clipboard.writeText(text).then(
function(){toast('地址已复制')},function(){toast(text)})
}else{toast(text)}});

loadWallpaper();loadInfo();loadFiles();
setInterval(function(){if(!document.hidden)loadFiles()},5000);
})();
</script>
</body>
</html>
"""
