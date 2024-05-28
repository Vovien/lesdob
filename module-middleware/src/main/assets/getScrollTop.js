function getDoctorTag() {
    let cataloglist = new Array();
    let ahriHtag = new Array();
    let h2 = document.getElementsByTagName("h2");
    for (let m = 0; m < h2.length; m++) {
        ahriHtag.push(h2[m]);
    }
    //根据视口高度来排序
    let newArry = ahriHtag.sort(function (a, b) {
        return a.offsetTop - b.offsetTop;
    });
    for (let i = 0; i < newArry.length; i++) {
        cataloglist.push({
            title: HtagReplace(newArry[i].innerHTML),
            type: newArry[i].nodeName,
            offsetTop: getElementTop(newArry[i])
        });
    }
    console.log(cataloglist)
    return cataloglist
}
function getHospitalTag() {
    let cataloglist = new Array();
    let ahriHtag = new Array();
    let h2 = document.getElementsByTagName("h2");
    let h3 = document.getElementsByTagName("h3");
    for (let m = 0; m < h2.length; m++) {
        ahriHtag.push(h2[m]);
    }
    for (let n = 0; n < h3.length; n++) {
        ahriHtag.push(h3[n]);
    }
    //根据视口高度来排序
    let newArry = ahriHtag.sort(function (a, b) {
        return a.offsetTop - b.offsetTop;
    });
    for (let i = 0; i < newArry.length; i++) {
        cataloglist.push({
            title: HtagReplace(newArry[i].innerHTML),
            type: newArry[i].nodeName,
            offsetTop: getElementTop(newArry[i])
        });
    }
    console.log(cataloglist)
    return cataloglist
}
function getElementTop(el) {
    let actualTop = el.offsetTop;
    let current = el.offsetParent;
    while (current !== null) {
        actualTop += current.offsetTop;
        current = current.offsetParent;
    }
    return actualTop;
}
function HtagReplace(innerText) {
    innerText = innerText.replace(/<\/?.+?\/?>/g, "");
    innerText = innerText.replace(/&nbsp;/g, "");
    return innerText;
}
window.onload = function () {
    getDoctorTag()
    getHospitalTag()
}