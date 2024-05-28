const indexNum = 2;
//注意：正文必须先去掉空格和换行 否则广告节点找不到 str = str.replace(/\n|\r/g, "");

/**
    * @author: Lauco
    * @description:第二个H2标签之前  不满足规则直接加在末尾
    * @param {string} nodeName  节点类名
    * @param {object} data  广告渲染字段
    */
function setAdUI(nodeName, data) {
    data = JSON.parse(data)
    let H2Dom = document.querySelectorAll(`${nodeName}>h2`);
    if (H2Dom.length >= indexNum) {
        let dom = H2Dom[indexNum - 1]
        if (dom.previousSibling) {
            addAdEle(dom.previousSibling, data)
        } else {
            addAdEle(dom, data)
        }
    } else {// 不满足规则直接加在末尾
        let nodeDom = document.querySelectorAll(nodeName)[0];
        addAdEle(nodeDom.lastElementChild, data)
    }
}
/**
 * @author: Lauco
 * @description: 添加广告
 * @param {any} dom
 * @param {object} data  广告渲染字段
 */
function addAdEle(dom, data) {
    let newElement = document.createElement("div");
    newElement.classList = "swiper_ad_yxb_box"
    let content = `<div class="swiper_ad_yxb_title">${data.title}</div><div class="swiper ad_swiper"><div class="swiper-wrapper">`
    data.list.forEach(item => {
        content += `
            <div class="swiper-slide swiper_slide">
            <div class="swiper_slide_left" style="background:url(${item.cover}) center / cover no-repeat;">
                <div class="slide_left_info">
                    <div class="slide_left_title"></div>
                    <div class="slide_left_content">${item.title}</div>
                </div>
            </div>
            <div class="swiper_slide_right">
                <div class="slide_right_title">${item.cost_title}</div>
                <div class="slide_right_content_spend">${item.cost_str}</div>
                <div class="slide_right_title">${item.disease_name_title}</div>
                <div class="slide_right_content_disease">${item.disease_name}</div>
                <div class="slide_right_title">${item.hospital_name_title}</div>
                <div class="slide_right_content_hospital">${item.hospital_name}</div>
            </div>
        </div>`
    });
    content += `</div></div><div class="swiper_pagination"></div>`
    newElement.innerHTML = content
    insertAfter(newElement, dom);
    initSwiper(data.list)
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
 * @description: 初始化swiper轮播
 * @param {any[]} list slide数组 点击swiper下标时返回给原生的数据
 */
function initSwiper(list) {
    new Swiper(".ad_swiper", {
        spaceBetween: 20,//间距
        loop: true,//循环
        autoplay: true,//自动播放
        delay: 3000,//n秒切换一次
        pagination: {
            el: ".swiper_pagination",
        },
        on: {
            click: function (e) {
                let { realIndex } = e
                console.log(`点击第${realIndex + 1}个广告`, list[realIndex])
                // window.webkit.messageHandlers.adClick.postMessage({ "index": realIndex, "data": JSON.stringify(list[realIndex]) })
                 window.imageLoader.advArticleClick(JSON.stringify(list[realIndex]));
            },
        },
    });
}
// setAdUI(".content", {
//     "title": "好孕案例", //标题
//     "list": [
//         {
//             "id": 159,
//             "cover": "https://image.jmbon.com/uploads/advertise_originality_ad_intention_cover/20230630/04v1VARp1T1688093081063.png", //封面
//             "experience_id": 12092, //经验贴id
//             "cover_title": "真实经验", //封面图标题
//             "cost": 35400, //花费
//             "title": "盆腔粘连甚比“千层饼”，层层粘住！", //标题
//             "cost_title": "花费", //花费标题
//             "hospital_name": "邢台不孕不育专科医院", //医院
//             "hospital_name_title": "医院", //医院标题
//             "disease_name": "残角子宫", //疾病
//             "disease_name_title": "疾病" //疾病标题
//         },
//         {
//             "id": 286,
//             "cover": "https://image.jmbon.com/uploads/advertise_originality_ad_intention_cover/20230703/xD0hMIxt7D1688365012080.png", //封面
//             "experience_id": 12680, //经验贴id
//             "cover_title": "真实经验", //封面图标题
//             "cost": 210400, //花费
//             "title": "相信奇迹！四十多岁的她赴美试管好孕成真！", //标题
//             "cost_title": "花费", //花费标题
//             "hospital_name": "美国加州生殖中心", //医院
//             "hospital_name_title": "医院", //医院标题
//             "disease_name": "多次流产", //疾病
//             "disease_name_title": "疾病" //疾病标题
//         }
//     ]
// })