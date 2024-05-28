/**
 * 初始化加载
 */
window.onload = function () {
    //获取图片DOM数组
    let myImg = document.getElementsByClassName("editorImg");
    //获取到图片的详细信息
    for (let i = 0; i < myImg.length; i++) {
        let actualsrc = myImg[i].getAttribute("data-actualsrc")
        let img = new Image();
        img.src = actualsrc;
        imgOnload(myImg, img, actualsrc, i);
    }
    //获取到视频占位图 app点击后跳转到相应视频流播放
    let replaceVideoList = document.getElementsByClassName("placeholderVideo")
    for (let i = 0; i < replaceVideoList.length; i++) {
        let src = replaceVideoList[i].getAttribute("data-src")
        let poster = replaceVideoList[i].getAttribute("data-poster")
        let videoId = replaceVideoList[i].getAttribute("data-aliyunVideoId")
        let itemId = replaceVideoList[i].getAttribute("data-itemid")
        let itemType = replaceVideoList[i].getAttribute("data-itemtype")
        replaceVideoList[i].onclick = function () {
            window.imageLoader.showVideo(src, poster, videoId, itemId, itemType);
        }
    }
};
/**
 * 加载图片资源，图片路径正常替换为真实地址，反之将显示图片加载失败占位
 * @param {Array} myImg 所有的图片数组
 * @param {Object} img 当前需加载的图片资源
 * @param {String} actualsrc 当前加载图片的真实地址
 * @param {Number} i 当前下标
 */
function imgOnload(myImg, img, actualsrc, i) {
    myImg[i].onclick = function () {
        let Prevarr = []
        for (let j = 0; j < myImg.length; j++) {
            let Prevsrc = myImg[j].src
            if (Prevsrc.substr(0, 4) == "data") {
                Prevsrc = "error"
            }
            Prevarr.push(Prevsrc)
        }
        console.log(Prevarr, i)
        window.imageLoader.showImage(JSON.stringify(Prevarr), i);
    }
    img.onload = function () {
        myImg[i].src = actualsrc;
    };
    img.onerror = function () {
        creatLoadNode(myImg, actualsrc, i)
    }
}
/**
 * 创建图片加载失败节点 errNode
 * @param {Array} myImg 所有的图片数组（除开审核占位）
 * @param {String} actualsrc 当前图片的真实地址
 * @param {Number} index 当前下标
 */
function creatLoadNode(myImg, actualsrc, index) {
    console.log(myImg, actualsrc, index)
    let createDiv = document.createElement('div');
    createDiv.innerHTML = "<div class='errNode'></div>";
    createDiv.setAttribute("id", 'imgerr_' + index)
    createDiv.className = "virtualNode";
    document.getElementsByClassName("editorImgBox")[index].appendChild(createDiv);
    createDiv.onclick = function () {
        let img = new Image();
        img.src = actualsrc;
        img.onload = function () {
            document.getElementById("imgerr_" + index).style.display = "none"
            myImg[index].src = actualsrc;
        };
    }
}
