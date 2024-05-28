package com.jmbon.middleware.utils

/**
 * @author : leimg
 * time   : 2022/8/24
 * desc   :
 * version: 1.0
 */
object UMEventKey {
    val Event_DetailsPage_Article_View = "Event_DetailsPage_Article_View" //,用户在文章详情页停留时长, 0

    val Event_LoginPage = "Event_LoginPage" //, 未注册的用户进入登录页面, 1
    val Event_NumbersQuickLoginPage_Code = "Event_NumbersQuickLoginPage_Code"//, 用户成功登录, 0
    val Event_MyNumberButton_Click = "Event_MyNumberButton_Click"//, 点击本机号码登录按钮, 1
    val Event_TogglerIcon_Click = "Event_TogglerIcon_Click"//, 点击切换手机号图标, 1
    val Event_WechatIcon_Click = "Event_WechatIcon_Click"//, 点击微信登录图标, 1
    val Event_AppleIcon_Click = "Event_AppleIcon_Click"//, 点击苹果登录图标, 1
    val Event_PhoneNumberButton_Click = "Event_PhoneNumberButton_Click"//, 点击手机号登录按钮, 1
    val Event_WechatButton_Click = "Event_WechatButton_Click"//, 点击微信按钮, 1
    val Event_PhoneLoginPage_InputBox_Enter = "Event_PhoneLoginPage_InputBox_Enter"//, 输入手机号, 1
    val Event_PhoneLoginPage_GetCodeButton_Click =
        "Event_PhoneLoginPage_GetCodeButton_Click"//, 点击获取验证码按钮, 1
    val Event_EnterCodePage_InputBox_Enter = "Event_EnterCodePage_InputBox_Enter"//, 填充验证码, 1
    //val Event_LoginPage_WechatIcon_Click = "Event_LoginPage_WechatIcon_Click"//, 点击微信登录, 1
    val Event_BindPhonePage_GetCodeButton_Click =
        "Event_BindPhonePage_GetCodeButton_Click"//, 在绑定手机号页面点击获取验证码, 1
    val Event_LoginPage_AppleIcon_Click = "Event_LoginPage_AppleIcon_Click"//, 点击苹果登录, 1
    val Event_BindPhonePage_InputBox_Enter = "Event_LoginPage_AppleIcon_Click"//, 在绑定手机号页面输入手机号, 1
    val Event_HomePage_CommonProblem_Icon_Click =
        "Event_HomePage_CommonProblem_Icon_Click"//, 点击常见问题对应位置图标, 0

    val Event_HomePage_8Icon_Click = "Event_HomePage_8Icon_Click"//, 点击更多图标, 1
    val Event_SGArticle_View = "Event_SGArticle_View"// 普通文章详情停留时长, 0
    val Event_SGScienceArticle_View = "Event_SGScienceArticle_View"//, 百科文章详情停留时长, 0
    val Event_CommonProblemPage_FirstNav = "Event_CommonProblemPage_FirstNav"//, 在常见问题详情页点击一级导航, 0
    val Event_CommonProblemPage_SecondNav = "Event_CommonProblemPage_SecondNav"//, 在常见问题详情页点击二级导航, 0
    val Event_ColumnDetails_Article_Click =
        "Event_ColumnDetails_Article_Click"//, 在专栏详情页点击文章卡片, 0
    val Event_HomePage_RecommendCard_Click = "Event_HomePage_RecommendCard_Click"//, 点击推荐知识卡片, 0
    val Event_HomePage = "Event_HomePage"//, 用户进入首页, 0
    val Event_HomePage_ChangeBatch_Click = "Event_HomePage_ChangeBatch_Click"//, 用户在推荐知识点击换一批, 1
    val Event_HomePage_BuretProcessCard_Click =
        "Event_HomePage_BuretProcessCard_Click"//, 用户在首页点击LesDoB卡片, 0
    val Event_BuretProcessPage_TimeCard_Click =
        "Event_BuretProcessPage_TimeCard_Click"//, 用户在试管流程详情点击时间表卡片, 1
    val Event_BuretProcessPage_FlowChart_Click =
        "Event_BuretProcessPage_FlowChart_Click"//, 用户在试管流程详情点击流程图卡片, 1
    val Event_BuretProcessPage_FirstNav = "Event_BuretProcessPage_FirstNav"//, 用户在试管流程详情页面点击一级导航, 0
    val Event_BuretProcessPage_SecondNav =
        "Event_BuretProcessPage_SecondNav"//, 用户在试管流程详情页面点击二级导航, 0
    val Event_FertilityGuidePage_ColumnCard_Click =
        "Event_FertilityGuidePage_ColumnCard_Click"//, 用户在助孕指南详情点击专栏卡片, 0
    val Event_HospitalDetails_enter = "Event_HospitalDetails_enter"//, 用户进入医院详情页, 1
    val Event_CallIcon_click = "Event_CallIcon_click"//, 用户在医院详情页拨打电话, 1
    val Event_CopyAddress_click = "Event_CopyAddress_click"//, 用户在医院详情页复制地址, 1
}
