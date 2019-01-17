$(document).ready(function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('业务管理', '需求管理', '', '访问');
    // step 1：个性化本页面的特殊风格
    initStyle();
    // step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
    initEvent();
    // step 3：获取默认首次加载的初始化参数，并给隐藏form赋值
    initDefaultParams();
    jurisdiction();
  });
  
  
  
  //step 1: 个性化本页面的特殊风格
  function initStyle() {
    //输入框默认为空
    $("#newWarp input,#replyWarp input").val("");
    $("#newWarp textarea,#replyWarp textarea").val("");
    //问题名称：不超过30字
    $("#newWarp .Qname").attr("maxlength", "30");
    //问题描述；不超过200字
    $("#newWarp .Qdescr").attr("maxlength", "200");
    //联系人
    $("#newWarp .contacts,#replyWarp .contacts").attr("maxlength", "20")
    //手机号
    $("#Qtel,#Rtel").change(function(){
        $(this).siblings("span").remove();
        var sMobile = $(this).val();
        //var regTel=/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1})|(14[0-9]{1}))+\d{8})$/;//定义手机号正则表达式
        var regTel=/^1(3|4|5|7|8)\d{9}$/;
        //var regphone=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
        // 区号+座机号码+分机号码
        var regphone=/^$|(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
        if (sMobile.substring(0, 1) == 1) {
            if (!regTel.test(sMobile) || sMobile.length != 11) {
                if($(this).siblings("span").length==0){
                    $(this).after("<span style='color:red'>手机号码格式不正确！请重新输入！</span>"); 
                }
                $(this).val("");
                //$(this).focus();
                return false;
            }else{
                $(this).siblings("span").remove();
            }
        }else if (sMobile.substring(0, 1) == 0) {
                if (!regphone.test(sMobile)) {
                    if($(this).siblings("span").length==0){
                        $(this).after("<span style='color:red'>手机号码格式不正确！请重新输入！</span>"); 
                    }
                    $(this).val("");
                    //$(this).focus();
                    return false;
                }else{
                    $(this).siblings("span").remove();
                }
        }else{
            if($(this).siblings("span").length==0){
                $(this).after("<span style='color:red'>手机号码格式不正确！请重新输入！</span>"); 
            }
            $(this).val("");
            //$(this).focus();
            return false;
        }
    }).attr("maxlength","13");
    //邮箱
    $("#Qemail,#Remail").change(function(){
        $(this).siblings("span").remove();
        var email=$(this).val();
         var regEmail=/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        var regECh=/^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z]+))@([a-zA-Z0-9-]+[.])+([a-zA-Z]{2}|net|NET|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT)$/;
        if (email.search(regECh) != -1) { 
            // if(!regEmail.test(email)){
            //     $(this).val("");
            //     if($(this).siblings("span").length==0){
            //         $(this).after("<span style='color:red'>邮箱格式不正确！请重新输入！</span>"); 
            //     }
            //     $(this).focus(); 
            //     return false;
            // }else{
                $(this).siblings("span").remove();
                return true; 
            // }
        } else { 
            $(this).val("");
            if($(this).siblings("span").length==0){
                $(this).after("<span style='color:red'>邮箱格式不正确！请重新输入！</span>"); 
            }
            //$(this).focus(); 
            return false;
        } 
    });
    //输入框非空校验
    $("#newWarp .newWarp,#replyWarp .replyWarp").on("blur","li input,li textarea",function(){
        if($(this).val()==""){
            if($(this).siblings("span").length==0){
                $(this).val("");
                $(this).after("<span style='color:red'>不能为空！</span>");
            }
            //$(this).focus(); 
            return false; 
        }else{
            $(this).siblings("span").remove();
            $(this).attr("title",$(this).val())
            return true; 
        }
    });

    //取消按钮
    $("#problemForm").on("click",".closeSub",function(){
        window.close();
        //刷新父页面
        window.opener.location.reload();
    })

    //已解决按钮
    $("#resultWarp .resultForm").on("click",".resolved",function(){
        $.ajax({
            url: '/cmca/wtfk/checkLogin',
            dataType: 'json',
            cache: false,
            success: function (data) {
                if (data.islogin == "1") {
                    //已解决
                    solveData();
                }else{
                  //登录失效
                  alert("登录已失效，请重新登录");
                  window.open('/cmca/home/index', "_self");
                }}
              }
            );
        
    });

    //更新问题
    $("#resultWarp .resultForm").on("click",".updata",function(){
        sessionStorage.setItem("wtfkdata", "reQ");
        sessionStorage.setItem("wtfkreqId", $("#reqId").val());
        $(window).attr('location','/cmca/wtfk/feedback')
        getCookie();
    });


  }
  
  //step 2：绑定页面元素的响应时间,比如onclick,onchange,hover等
  function initEvent() {
    //每一个事件的函数按如下步骤：
    //1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
  
    
  
  }
  
  //step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
  function initDefaultParams() {
    getCookie() 
     // 请求权限
    var htmlName = $(".problemFormList").attr("id");
    switch (htmlName) {
        case "newQ"://问题新增
            // $("#replyWarp").remove();//移除反馈
            // $("#resultWarp").remove();//移除反馈答复
            $("#newWarp").removeClass("hide");
            selectList1();
            selectList2();
            //图片上传1
            imgUpload1();
        break;
        case "fkQ"://问题反馈
            // $("#newWarp").remove();//移除新增
            // $("#resultWarp").remove();//移除反馈答复
            $("#replyWarp").removeClass("hide");
            selectList1();
            selectList2();
            //图片上传
            imgUpload2();
            //回填问题信息
            getQDetails();
            getQDetailsImg();
        break;
        case "fkdfQ"://问题反馈答复
            // $("#newWarp").remove();//移除新增
            // $("#replyWarp").remove();//移除反馈
            $("#resultWarp").removeClass("hide");
            //回填问题及反馈信息
            getRDetails();
            getRDetailsImg();
        break;
        case "reQ"://更新问题
            // $("#replyWarp").remove();//移除反馈
            // $("#resultWarp").remove();//移除反馈答复
            $("#newWarp").removeClass("hide");
            selectList1();
            selectList2();
            $(".newForm .submit").attr("id","upSubmit");
            //图片上传1
            imgUpload1();
            //更新问题
            reDataList();
            reDataListImg();
        break;
        case "lookQ"://查看
            // $("#newWarp").remove();//移除新增
            // $("#replyWarp").remove();//移除反馈
            $("#resultWarp").removeClass("hide");
            $(".resultForm").remove();
            
            //查看详情
            getLook();
            getRDetailsImg();
        break;
    }
    
  }