var PDomArr = [];
var validLen = 0;
var maxNum = 2;
//注意：正文必须先去掉空格和换行 否则广告节点找不到 str = str.replace(/\n|\r/g, "");

/**
    * @author: Lauco
    * @description:在正文中插入图片广告（不能出现在html中）
    * 1.查询内容中 p 标签是否≥2，如果满足此规则将广告图放置在第2个p标签下(图片p 和图片描述p 不计入在内)
    * 注意：插入的广告图不能与正文图片挨在一起
    * 举例：当满足 p≥2，此时广告图位于第2个p下方，但是第2个p后刚好接着一张图片，那么就不能将广告图放置在第2个p下；
    *      此时需要查询第1个p，并且第1个p也符合条件（不能与正文图片挨在一起），那么就放置在第1个p标签下；
    * 2.如果不满足图片广告规则，就不做处理，无需添加广告
    * @param {string} nodeName  节点类名
    * @param {object} data  广告渲染字段
    */
function setAdUI(nodeName, data) {
    data = JSON.parse(data)
    let pDom = document.querySelectorAll(`${nodeName}>p`);
    pDom.forEach((item, index) => {
        //图片p 和图片描述p 不计入在内
        if (isImageOrAlt(item)) {
            PDomArr.push({ dom: item, valid: false });
        } else {
            validLen++;
            PDomArr.push({ dom: item, valid: true, validLen });
        }
    });
    if (validLen >= maxNum) {
        let findObj = PDomArr.find((item) => item.validLen == maxNum);
        isHasPrevAndNext(nodeName, findObj.dom, data, true);
    }
}
/**
 * @author: Lauco
 * @description: 是否为图片或者图片alt描述
 * @param {any} dom
 * @return {boolean}
 */
function isImageOrAlt(dom) {
    if (dom.firstChild.className == "editorImg") {
        return true;
    } else if (// 图片描述是P并且是居中的
        dom.nodeName == 'P' &&
        dom.style[0] == "text-align" &&
        dom.previousSibling &&
        dom.previousSibling.firstChild.className == "editorImg"
    ) {
        return true;
    } else {
        return false;
    }
}
/**
 * @author: Lauco
 * @description: 下一个节点是否属于图片
 * @param {string} nodeName  节点类名
 * @param {any} dom
 * @param {object} data  广告渲染字段
 * @param {boolean} isP2 是否是第2个p节点
 */
function isHasPrevAndNext(nodeName, dom, data, isP2) {
    //如果属于图片，需要对p1进行处理
    if (dom.nextSibling && dom.nextSibling.firstChild.className == "editorImg") {
        if (isP2) {
            let findObj = PDomArr.find((item) => item.validLen == 1);
            isHasPrevAndNext(nodeName, findObj.dom, data);
        }
    } else {
        let contentWidth = document.querySelector(`${nodeName}`);//需要获取高度，图片高度是宽度的一半
        addImageEle(dom,
            `<div class="image_ad_yxb_box">
         <div class="image_ad_yxb_content" style="height:${contentWidth.offsetWidth / 2}px;background:url(${data.cover}) center / cover no-repeat;"></div>
        <div class="image_ad_yxb_qrcode_box">
          <div class="image_ad_yxb_qrcode">
            <div class="image_ad_yxb_qrimg" style="background: url(${data.qrcode}) center / cover no-repeat"></div>
          </div>
          <div class="image_ad_yxb_qrcode_txet">${data.qrcode_title}</div>
        </div>
        <div class="image_ad_yxb_layer">
          <div class="image_ad_yxb_title">${data.title}</div>
          <div class="image_ad_yxb_info">
            <div class="image_ad_yxb_info_case">${data.sub_title1}</div>
            <div class="image_ad_yxb_info_share">${data.sub_title2}</div>
          </div>
          <div class="image_ad_yxb_copyright">${data.statement}</div>
          <div class="image_ad_yxb_slogan"></div>
        </div>
      </div>`,
            data
        )
    }
}
/**
 * @author: Lauco
 * @description: 添加图片广告
 * @param {any} dom
 * @param {string} content  文字链html string结构
 * @param {object} data  广告渲染字段
 */
function addImageEle(dom, content, data) {
    let newElement = document.createElement("p");
    newElement.style.textAlign = "center"
    newElement.innerHTML = content;
    insertAfter(newElement, dom);
    setAdCardEvent(data);
}

/**
 * @author: Lauco
 * @description:插入节点
 * @param {any} newElement 新节点
 * @param {any} targetElement  参照节点
 */
function insertAfter(newElement, targetElement) {
    let parent = targetElement.parentNode;
    if (parent.lastChild == targetElement) {
        parent.appendChild(newElement);
    } else {
        parent.insertBefore(newElement, targetElement.nextSibling);
    }
}
/**
 * @author: Lauco
 * @description: 点击卡片事件
 * @param {object} data  广告渲染字段
 */
function setAdCardEvent(data) {
    let adDom = document.querySelectorAll(`.image_ad_yxb_box`)[0];
    adDom.onclick = function () {
        console.log("点击广告", data)
//        window.webkit.messageHandlers.adClick.postMessage({ "data": JSON.stringify(data) })
         window.imageLoader.adClick(JSON.stringify(data));
    }
}

//setAdUI(".content", {
//     "id": 93,
//     "cover": "https://image.jmbon.com/uploads/advertise_originality_ad_intention_cover/20230629/c0kEj8dDt21688022433752.png", //封面
//     "title": "8促2移！“明白+行动+坚持”就会春暖花开！", //标题
//     "sub_title1": "#孕小帮APP授权案例", //子标题1
//     "sub_title2": "@感谢宝妈豆姐无私分享", //子标题2
//     "statement": "图片及视频授权 孕小帮 唯一使用", //声明
//     "qrcode": "https://image.jmbon.com/uploads/advertise_landing_page_qrcode/20230330/YPcuSxXmNO1680158337456.jpg", //二维码
//     "qrcode_title": "点击添加生育顾问微信，获取完整案例", //二维码标题
//     "item_type": "programme_popup", //跳转类型【programme_popup：获取方案弹窗】
//     "identity": 0
//})