$(document).ready(function () {
  // 插入一经事件码-查询
  dcs.addEventCode('MAS_HP_CMCA_child_query_02');
  // 日志记录
  get_userBehavior_log('系统管理', '权限查询', '', '访问');

  // step 2：个性化本页面的特殊风格
  initStyle();
  // step 3：绑定本页面元素的响应时间,比如onclick,onchange,hover等
  initEvent();
  // step 4：获取默认首次加载的初始化参数，并给隐藏form赋值
  initDefaultParams();
});

//step 2: 个性化本页面的特殊风格
function initStyle() {
  //TODO 自己页面独有的风格
}

//step 3：绑定页面元素的响应时间,比如onclick,onchange,hover等
function initEvent() {
  //每一个事件的函数按如下步骤：
  //1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载

  // 顶部查询按钮-点击
  $('#searchBtn').click(function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    var user = $('#userList').val() === null ? null : $('#userList').val().join(','),
      prvd = $('#prvdList').val() === null ? null : $('#prvdList').val().join(','),
      department = $('#departmentList').val() === null ? null : $('#departmentList').val().join(',');
    if (user == null && prvd == null && department == null) {
      alert('请选择查询条件');
    } else {
      // 插入一经事件码-查询
      dcs.addEventCode('MAS_HP_CMCA_child_query_02');
      // 日志记录
      get_userBehavior_log('系统管理', '权限查询', '查询', '导出');

      // 请求查询结果
      load_search_control_result_table(user, prvd, department);
    }
  });

  // 顶部导出按钮-点击
  $('#exportBtn').click(function () {
    // 插入一经事件码-下载
    dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
    // 日志记录
    get_userBehavior_log('系统管理', '权限查询', '文件导出', '查询');

    var userVal = $('#userList').val() === null ? '' : $('#userList').val().join(','),
      prvdVal = $('#prvdList').val() === null ? '' : $('#prvdList').val().join(','),
      departmentVal = $('#departmentList').val() === null ? '' : $('#departmentList').val().join(','),
      locationInfo = window.location.toString();

    if (userVal == '' && prvdVal == '' && departmentVal == '') {
      alert('请选择查询条件');
    } else {
      if (locationInfo.indexOf('Mine') != -1) { // 我的权限
        window.open('/cmca/qxcx/outPutUser?prvd=' + prvdVal + '&department=' + departmentVal + '&user=' + userVal + '');
      } else { // 权限管理
        window.open('/cmca/qxcx/outPutUserByOption?prvd=' + prvdVal + '&department=' + departmentVal + '&user=' + userVal + '');
      }
    }
  });
}

//step 4.获取默认首次加载的初始化参数，并给隐藏form赋值
function initDefaultParams() {
  var strategyList = $('#strategyList'),
    userList = $('#userList'),
    prvdList = $('#prvdList'),
    departmentList = $('#departmentList'),
    locationInfo = window.location.toString(),
    personControl = JSON.parse(sessionStorage.getItem('rightControl')),
    defaultUser, defaultPrvd, defaultDepartment;
  // 判断是从哪个链接跳转到权限查询页面
  // 从我的权限进入
  if (locationInfo.indexOf('Mine') != -1) {
    // 将用户信息展示在筛选列表中
    $.ajax({
      url: '/cmca/qxcx/getUserInfoById',
      async: false,
      dataType: 'json',
      success: function (data) {
        // 设置默认选中项
        defaultUser = data.userList[0].id;
        defaultPrvd = data.prvdList[0].id;
        defaultDepartment = data.departmentList[0].id;
        // 判断是否拥有权限查询界面的权限
        if (personControl.cxsjqxcx) {
          $.ajax({
            url: '/cmca/qxcx/getUserInfo',
            async: false,
            dataType: 'json',
            success: function (data) {
              // 加载数据
              userList.selectpickerLoadData(data.userList);
              prvdList.selectpickerLoadData(data.prvdList);
              departmentList.selectpickerLoadData(data.departmentList);
            }
          });
        } else {
          // 加载数据
          userList.selectpickerLoadData(data.userList);
          prvdList.selectpickerLoadData(data.prvdList);
          departmentList.selectpickerLoadData(data.departmentList);
        }
        // 选项初始化
        userList.selectpickerInit(defaultUser);
        prvdList.selectpickerInit(defaultPrvd);
        departmentList.selectpickerInit(defaultDepartment);
        // 请求当前登录用户权限
        load_search_control_result_table(defaultUser, defaultPrvd, defaultDepartment);
      }
    });
  } else { // 从权限管理进入
    // 获取筛选信息列表
    $.ajax({
      url: '/cmca/qxcx/getUserInfo',
      async: false,
      dataType: 'json',
      success: function (data) {
        userList.selectpickerLoadData(data.userList);
        prvdList.selectpickerLoadData(data.prvdList);
        departmentList.selectpickerLoadData(data.departmentList);
      }
    });
  }
}
// 数据模块单独放在data.js文件中