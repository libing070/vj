/**
 * 云通信基础能力业务短信发送、查询详情以及消费消息示例，供参考。
 * Created on 2017-07-31
 */
/*const SMSClient = require('@alicloud/sms-sdk');
// ACCESS_KEY_ID/ACCESS_KEY_SECRET 根据实际申请的账号信息进行替换
const accessKeyId = 'LTAIzW3SSsHK1L6u';
const secretAccessKey = 'hWL6whFkwgS59T8ovgsUBEVxSQffih';
//初始化sms_client
let smsClient = new SMSClient({accessKeyId, secretAccessKey});
let sendsmsController = {};
sendsmsController.sendsms = (req, res)=>{
  //发送短信
  smsClient.sendSMS({
    PhoneNumbers: req.body.phone,//必填:待发送手机号。”
    SignName: '波英冰',//必填:短信签名-可在短信控制台中找到
    TemplateCode: 'SMS_149416563',//必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
    TemplateParam: '{"code":"libing"}'//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时。
  }).then(function (res) {
    let {Code}=res;

    if (Code === 'OK') {
      //return res.json({"errcode": 40001, "type": 'error',"errmsg": "注册失败"});
      //处理返回参数
      console.log("yzm:"+res)
    }
  }, function (err) {

    console.log(err)
  })
}*/

var QcloudSms = require("qcloudsms_js");

// 短信应用SDK AppID
var appid = 1400156472;  // SDK AppID是1400开头

// 短信应用SDK AppKey
var appkey = "ed88e4cd7530ed6f67f059aaa9d34a3e";

// 需要发送短信的手机号码
var phoneNumbers = ["21212313123", "12345678902", "12345678903"];

// 短信模板ID，需要在短信应用中申请
var templateId = 7839;  // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
//templateId 7839 对应的内容是"您的验证码是: {1}"
// 签名
var smsSign = "";  // NOTE: 这里的签名只是示例，请使用真实的已申请的签名, 签名参数使用的是`签名内容`，而不是`签名ID`

// 实例化QcloudSms
var qcloudsms = QcloudSms(appid, appkey);

let sendsmsController = {};



sendsmsController.sendsms = (req, res)=>{
  var ssender = qcloudsms.SmsSingleSender();
  var params = ["5678"];//数组具体的元素个数和模板中变量个数必须一致，例如事例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
  ssender.sendWithParam(86, req.body.phone, templateId,
    params, SmsSign, "", "", callback);  // 签名参数未提供或者为空时，会使用默认签名发送短信

// 设置请求回调处理, 这里只是演示，用户需要自定义相应处理回调
  function callback(err, res, resData) {
    if (err) {
      console.log("err: ", err);
    } else {
      console.log("request data: ", res.req);
      console.log("response data: ", resData);
    }
  }
};
module.exports = sendsmsController;
