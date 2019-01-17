// 以下为DOM模板
/**
 * 组件模板
 */
function component_item_template(componentID, idx) {
    var titleBgs = ['green_heading', 'light_blue_heading', 'blue_heading', 'yellow_heading', 'light_violet_heading', 'violet_heading'];
    // 返回模板
    return '<!-- 图表组件 -->' +
        '<div class="component assembly" id="' + componentID + '">' +
        '    <div class="panel panel-default">' +
        '        <!-- 组件标题 -->' +
        // '        <div class="panel-heading ' + titleBgs[idx] + '">' +
        // '        <div class="panel-heading">' +
        // '            <div class="panel-title">' +
        // '                <h5 class="component_title"></h5>' +
        // '            </div>' +
        '            <!-- 组件配置和删除按钮 -->' +
        '            <div class="btn-group btn-group-xs component_config_btn_group">' +
        '                <button type="button" class="btn component_config_btn" title="编辑组件">' +
        '                    <span class="glyphicon glyphicon-cog"></span>' +
        '                </button>' +
        '                <button type="button" class="btn component_remove_btn" title="删除组件">' +
        '                    <span class="glyphicon glyphicon-remove"></span>' +
        '                </button>' +
        '            </div>' +
        // '        </div>' +
        '        <!-- 配置区域 -->' +
        '        <div class="panel-body">' +
        '            <!-- 组件配置 -->' +
        '            <div class="col-xs-12 component_config hideUnit componentConfig">' +
        '                <form class="form-inline">' +
        '                    <div class="form-group">' +
        '                        <label>布局：</label>' +
        '                        <select class="selectpicker layoutList" id="layoutList" title="选择布局" data-size="5" data-width="115px">' +
        '                            <option value="1">1:1:1</option>' +
        '                            <option value="5">1:1</option>' +
        '                            <option value="2">1:2</option>' +
        '                            <option value="3">2:1</option>' +
        '                            <option value="4">1</option>' +
        '                        </select>' +
        '                    </div>' +
        // '                    <div class="form-group">' +
        // '                        <label>组件名称：</label>' +
        // '                        <input class="form-control component_name" type="text" value="" placeholder="请输入组件名称">' +
        // '                    </div>' +
        '                    <div class="form-group">' +
        '                        <button class="btn btn-primary componentSaveBtn" type="button">保存</button>' +
        '                    </div>' +
        '                </form>' +
        '            </div>' +
        '            <!-- 控件配置 -->' +
        '            <div class="col-xs-12 widget_config hideUnit widgetConfig">' +
        '                <form class="form-inline">' +
        '                    <div class="form-group" style="width:15%">' +
        '                        <label  class="text-right" style="width:35%;">图形选择:</label>' +
        '                        <select class="selectpicker choose_chart chartTypeList" title="选择图形" data-size="5" data-width="60%">' +
        '                            <option value="barCharts">柱形图</option>' +
        '                            <option value="lineCharts">折线图</option>' +
        '                            <option value="barAndLineCharts">柱状折线图</option>' +
        '                            <option value="pie">饼图</option>' +
        // '                            <option value="radarMap">雷达图</option>' +
        // '                            <option value="bubble">气泡图</option>' +
        // '                            <option value="map">地图</option>' +
        '                            <option value="chartTable">图表</option>' +
        '                        </select>' +
        '                    </div>' +
        '                    <div class="form-group" style="width:15%">' +
        '                        <label  class="text-right" style="width:35%;">控件名称:</label>' +
        '                        <input class="form-control chartWidgetName" type="text" value="" placeholder="输入控件名称" style="width:60%">' +
        '                    </div>' +
        '                    <div class="form-group" style="width:15%">' +
        '                       <div>' +
        '                           <label class="text-right" style="width:30%;">选择表:</label>' +
        '                           <select class="selectpicker form-control fr chartTableList" title="选择表" data-size="5" data-width="65%" disabled>' +
        '                               <!-- 后台加载表名称 -->' +
        '                           </select>' +
        '                       </div>' +
        '                       <div class="chartListList">' +
        '                           <div class="choose_list_x hideUnit" style="margin-top:5px;">' +
        '                               <label class="text-right" style="width:30%;">X轴:</label>' +
        '                               <select class="selectpicker form-control XListList" title="选择值" data-size="5" multiple disabled>' +
        '                                   <!-- 后台X轴 -->' +
        '                               </select>' +
        '                           </div>' +
        '                           <div class="choose_list_y hideUnit" style="margin-top:5px;">' +
        '                               <label class="text-right" style="width:30%;">Y轴:</label>' +
        '                               <select class="selectpicker form-control YListList" title="选择值" data-size="5" multiple disabled>' +
        '                                   <!-- 后台Y轴 -->' +
        '                               </select>' +
        '                           </div>' +
        '                       </div>' +
        '                    </div>' +
        '                    <div class="form-group" style="width:45%">' +
        '                       <div class="screen_logic_title screenTitle">' +
        '                           <ul class="list-inline clear_float text-center">' +
        '                               <li class="col-xs-3">筛选字段:</li>' +
        '                               <li class="col-xs-3">运算符:</li>' +
        '                               <li class="col-xs-3">值:</li>' +
        '                               <li class="col-xs-3">连接条件:</li>' +
        '                           </ul>' +
        '                       </div>' +
        '                       <div class="screen_logic_list">' +
        '                           <div class="screen_logic_item">' +
        '                               <ul class="list-inline clear_float">' +
        '                                   <li class="col-xs-3">' +
        '                                       <select class="selectpicker form-control col-xs-10 chartScreenList" title="选择筛选字段" data-size="5" data-width="100%">' +
        '                                           <!-- 后台加载筛选字段 -->' +
        '                                       </select>' +
        '                                   </li>' +
        '                                   <li class="col-xs-3">' +
        '                                       <select class="selectpicker form-control col-xs-10 operatorList" title="选择运算符" data-size="5" data-width="100%">' +
        '                                           <option value=">=">大于等于</option>' +
        '                                           <option value=">">大于</option>' +
        '                                           <option value="=">等于</option>' +
        '                                           <option value="<">小于</option>' +
        '                                           <option value="<=">小于等于</option>' +
        '                                           <option value="like">模糊匹配</option>' +
        '                                           <option value="*=" class="hide">取参数值</option>' +
        '                                       </select>' +
        '                                   </li>' +
        '                                   <li class="col-xs-3" data-toggle="tooltip" data-placement="right" title="值应为数字类型">' +
        '                                       <input class="form-control customVal" type="text" value="" placeholder="输入值" style="width:100%">' +
        '                                   </li>' +
        '                                   <li class="col-xs-3">' +
        '                                       <select class="selectpicker form-control col-xs-10 connectScreenList" data-width="100%">' +
        '                                           <option value="AND" selected>AND</option>' +
        '                                           <option value="OR">OR</option>' +
        '                                       </select>' +
        '                                   </li>' +
        '                               </ul>' +
        '                           </div>' +
        '                           <div class="screen_logic_item">' +
        '                               <ul class="list-inline clear_float">' +
        '                                   <li class="col-xs-3">' +
        '                                       <select class="selectpicker form-control col-xs-10 chartScreenList" title="选择筛选字段" data-size="5" data-width="100%">' +
        '                                           <!-- 后台加载筛选字段 -->' +
        '                                       </select>' +
        '                                   </li>' +
        '                                   <li class="col-xs-3">' +
        '                                       <select class="selectpicker form-control col-xs-10 operatorList" title="选择运算符" data-size="5" data-width="100%">' +
        '                                           <option value=">=">大于等于</option>' +
        '                                           <option value=">">大于</option>' +
        '                                           <option value="=">等于</option>' +
        '                                           <option value="<">小于</option>' +
        '                                           <option value="<=">小于等于</option>' +
        '                                           <option value="like">模糊匹配</option>' +
        '                                           <option value="*=" class="hide">取参数值</option>' +
        '                                       </select>' +
        '                                   </li>' +
        '                                   <li class="col-xs-3" data-toggle="tooltip" data-placement="right" title="值应为数字类型">' +
        '                                       <input class="form-control customVal" type="text" value="" placeholder="输入值" style="width:100%">' +
        '                                   </li>' +
        '                                   <li class="col-xs-3">' +
        '                                       <select class="selectpicker form-control col-xs-10 connectScreenList" data-width="100%">' +
        '                                           <option value="AND" selected>AND</option>' +
        '                                           <option value="OR">OR</option>' +
        '                                       </select>' +
        '                                   </li>' +
        '                               </ul>' +
        '                           </div>' +
        '                           <p><a href="javascript:void(0)" class="addScreenLogicBtn">添加筛选逻辑</a></p>' +
        '                       </div>' +
        '                    </div>' +
        '                    <div class="form-group" style="width:5%">' +
        '                        <button class="btn btn-primary chartWidgetSaveBtn" type="button">保存</button>' +
        '                    </div>' +
        '                </form>' +
        '            </div>' +
        '        </div>' +
        '        <!-- 控件展示区域 -->' +
        '        <div class="panel-footer charts_container clear_float widgetCtn">' +
        '            <!-- 控件-动态加载 -->' +
        '        </div>' +
        '    </div>' +
        '</div>';
}

/**
 * 控件布局模板
 * @param {Number} layout:组件宽度比例，bootstrap框架，栅格布局，
 * @param {String} controllID：控件ID,动态传入，在点击控件配置的时候使用，同时也是控件容器ID
 * @param {Number} idx：控件的index值，用于动态生成不同颜色的标题背景
 */
function widget_item_template(layout, controllID, idx) {
    // 动态生成标题背景颜色
    var titleBgs = ['green_heading', 'light_blue_heading', 'blue_heading', 'yellow_heading', 'light_violet_heading', 'violet_heading'];
    return '<div class="chart_item col-xs-' + layout + '" data="' + controllID + '">' +
        '       <div class="panel panel-default">' +
        '           <div class="panel-heading ' + titleBgs[idx] + '">' +
        '               <div class="panel-title">' +
        '                   <h6></h6>' +
        '               </div>' +
        '               <div class="btn-group btn-group-xs widget_config_btn fr">' +
        '                   <button type="button" class="btn btn-default config_btn widgetConfigBtn">' +
        '                       <span class="glyphicon glyphicon-cog"></span>' +
        '                   </button>' +
        '               </div>' +
        '           </div>' +
        '           <div class="panel-body">' +
        '               <div class="chart_wrap" id="Wrap' + controllID + '">' +
        '                   <div class="chart" id="' + controllID + '"></div >' +
        '               </div>' +
        '           </div>' +
        '        </div>' +
        '   </div>';
}

/**
 * 表格控件模板
 * @param {String} controllID：控件ID,动态传入，在点击控件配置的时候使用，同时也是控件容器ID
 */
function point_table_item_template(controllCtn) {
    return '<!-- 控件-动态加载 -->' +
        '<table class="table table-bodered table-striped table_item" id="' + controllCtn + '">' +
        '   <!-- table -->' +
        '</table>';
}

// 筛选逻辑模板
function widget_config_screen_template(idx) {
    return '<!-- 筛选字段逻辑表单 -->' +
        '<div class="screen_logic_item" data="screenForm' + idx + '">' +
        '   <ul class="list-inline clear_float">' +
        '       <li class="col-xs-3">' +
        '           <select class="selectpicker form-control col-xs-10 chartScreenList" title="选择筛选字段" data-size="5" data-width="100%">' +
        '               <!-- 后台加载筛选字段 -->' +
        '           </select>' +
        '       </li>' +
        '       <li class="col-xs-3">' +
        '           <select class="selectpicker form-control col-xs-10 operatorList" title="选择运算符" data-size="5" data-width="100%" disabled>' +
        '               <option value=">=">大于等于</option>' +
        '               <option value=">">大于</option>' +
        '               <option value="=">等于</option>' +
        '               <option value="<">小于</option>' +
        '               <option value="<=">小于等于</option>' +
        '               <option value="<=">包含</option>' +
        '               <option value="<=">不包含</option>' +
        '               <option value="*=" class="hide">取参数值</option>' +
        '           </select>' +
        '       </li>' +
        '       <li class="col-xs-3"  data-toggle="tooltip" data-placement="right" title="值应为数字类型">' +
        '           <input class="form-control customVal" type="text" value="" placeholder="输入值" style="width:100%" disabled>' +
        '       </li>' +
        '       <li class="col-xs-3">' +
        '           <select class="selectpicker form-control col-xs-10 connectScreenList" data-width="100%" disabled>' +
        '               <option value="AND" selected>AND</option>' +
        '               <option value="OR">OR</option>' +
        '           </select>' +
        '       </li>' +
        '   </ul>' +
        '   <div class="screen_item_remove_btn">' +
        '       <span class="glyphicon glyphicon-remove"></span>' +
        '   </div>' +
        '</div>';
}

// 新建tab页监控点模板
function tab_page_template(tabId) {
    return '<!-- 监控点tab页面 -->' +
        '<div id="' + tabId + '" class="tab-pane point_page">' +
        '    <ul class="nav nav-tabs point_page_tabs">' +
        '        <li class="active"><a href="#' + tabId + 'pointDetailsWrap" data-toggle="tab">监控点详情展示</a></li>' +
        '        <li><a href="#' + tabId + 'pointRanking" data-toggle="tab">排名汇总</a></li>' +
        '        <li><a href="#' + tabId + 'PointDetailedList" data-toggle="tab">监控清单</a></li>' +
        '    </ul>' +
        '    <div class="tab-content point_tab_content" style="height:90%">' +
        // 监控点详情展示
        '       <div class="tab-pane active point_tab_page" id="' + tabId + 'pointDetailsWrap" data="overview">' +
        '           <div id="' + tabId + 'pointDetails">' +
        //          顶部审计月筛选/新增组件按钮
        '               <div class="top_search clear_float">' +
        '                   <div class="fl">' +
        '                       <label for="chooseSpecial">审计月</label>' +
        '                       <select class="selectpicker audTrmList" title="选择审计月" data-size="5" data-width="115px">' +
        '                           <!-- 后台请求审计月数据 -->' +
        '                       </select>' +
        '                       <label for="chooseSpecial">页面布局版本</label>' +
        '                       <select class="selectpicker pointCodeVersionsList" title="选择版本" data-size="5" data-width="115px">' +
        '                           <!-- 后台请求当前监控点版本数据 -->' +
        '                       </select>' +
        '                   </div>' +
        '                   <div class="fr" style="margin-top:5px;">' +
        '                       <button class="btn btn-primary hideUnit editVersionsBtn">编辑版本</button>' +
        '                       <button class="btn btn-primary addVersionsBtn">新增版本</button>' +
        '                       <button class="btn btn-primary hideUnit addComponentBtn">新增组件</button>' +
        // 设置默认版本按钮需要根据权限来动态生成，未实现权限控制
        // '                <button class="btn btn-primary hideUnit saveDefaultVersionsBtn">设为默认版本</button>' +
        '                   </div>' +
        '               </div>' +
        '           </div>' +
        '        </div>' +
        // 监控点排名汇总
        '        <div class="tab-pane point_tab_page" id="' + tabId + 'pointRanking"  data="rank">' +
        '           <div class="top_search clear_float">' +
        '               <div class="fl">' +
        '                   <label for="choosePrvd">省公司：</label>' +
        '                   <select class="selectpicker pointPrvdList" title="" data-size="5" data-width="115px">' +
        '                       <!-- 后台请求省公司列表数据 -->' +
        '                   </select>' +
        '                   <label for="chooseSpecial">审计月</label>' +
        '                   <select class="selectpicker audTrmList" title="选择审计月" data-size="5" data-width="115px">' +
        '                       <!-- 后台请求审计月数据 -->' +
        '                   </select>' +
        '                   <label for="chooseSourceTable">选择类型</label>' +
        '                   <select class="selectpicker SourceTablelist" title="选择源表" data-size="5" data-width="135px">' +
        '                       <!-- 后台请求源表数据 -->' +
        '                   </select>' +
        '               </div>' +
        '               <div class="fr">' +
        '                   <ul class="point_sizer hide" data="Sizer">'+
        '                      <li data-toggle="modal" data-target="#' + tabId + 'myModal">添加筛选</li>'+
        '                      <li  data-toggle="black" data-target="#' + tabId + 'black">取消筛选</li>'+
        '                   </ul>'+
        '                   <button class="btn btn-primary" data-toggle="modalSizer" data-target="#' + tabId + 'myModal">筛选器</button>' +
        '                   <button class="btn btn-primary pointFiledDownBtn">文件下载</button>' +
        '               </div>' +
        '           </div>' +
        '           <table class="table table-bodered table-striped" id="' + tabId + 'pointRankingTable">' +
        '           </table>' +
        '        </div>' +
        // 监控点监控清单
        '        <div class="tab-pane point_tab_page" id="' + tabId + 'PointDetailedList" data="detail">' +
        '           <div class="top_search clear_float">' +
        '               <div class="fl">' +
        '                   <label for="choosePrvd">省公司：</label>' +
        '                   <select class="selectpicker pointPrvdList" title="全国" data-size="5" data-width="115px">' +
        '                       <!-- 后台请求省公司列表数据 -->' +
        '                   </select>' +
        '                   <label for="chooseSpecial">审计月</label>' +
        '                   <select class="selectpicker audTrmList" title="选择审计月" data-size="5" data-width="115px">' +
        '                       <!-- 后台请求审计月数据 -->' +
        '                   </select>' +
        '               </div>' +
        '               <div class="fr">' +
        '                     <ul class="point_sizer hide" data="Sizer">'+
        '                       <li data-toggle="modal" data-target="#' + tabId + 'myModal">添加筛选</li>'+
        '                       <li  data-toggle="black" data-target="#' + tabId + 'black">取消筛选</li>'+
        '                      </ul>'+
        '                   <button class="btn btn-primary" data-toggle="modalSizer" data-target="#' + tabId + 'myModal">筛选器</button>' +    
        '                   <button class="btn btn-primary pointFiledDownBtn">文件下载</button>' +
        '               </div>' +
        '           </div>' +
        '           <table class="table table-bodered table-striped" id="' + tabId + 'PointDetailedListTable">' +
        '           </table>' +
        '        </div>' +
        // < !--筛选弹出层 -->
        '   <div class="modal fade point_table_screen_modal" id="' + tabId + 'myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data="">' +
        '      <div class="modal-dialog">' +
        '           <div class="modal-content">' +
        '               <div class="modal-header">' +
        '                   <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
        // '                   <h4 class="modal-title" id="myModalLabel">模态框（Modal）标题</h4>' +
        '               </div>' +
        '               <div class="modal-body">' +
        '                   <ul class="list-unstyled row">' +
        '                       <li class="col-xs-12">' +
        '                           <ul class="list-inline text-center">' +
        '                               <li class="col-xs-4">筛选字段</li>' +
        '                               <li class="col-xs-4">运算符</li>' +
        '                               <li class="col-xs-4">值</li>' +
        '                           </ul>' +
        '                       </li>' +
        '                       <li class="col-xs-12 screen_logic_item">' +
        '                           <ul class="list-inline text-center">' +
        '                               <li class="col-xs-4">' +
        '                                   <select class="selectpicker pointScreenList screen_term" title="选择筛选字段" data-size="5" data-width="100%">' +
        '                                       <!-- 后台加载筛选字段 -->' +
        '                                   </select>' +
        '                               </li>' +
        '                               <li class="col-xs-4">' +
        '                                   <select class="selectpicker pointOperatorList screen_term" title="选择运算符" data-size="5" data-width="100%">' +
        '                                       <option value=">=">大于等于</option>' +
        '                                       <option value=">">大于</option>' +
        '                                       <option value="=">等于</option>' +
        '                                       <option value="<">小于</option>' +
        '                                       <option value="<=">小于等于</option>' +
        '                                       <option value="contains">包含</option>' +
        '                                       <option value="notcontains">不包含</option>' +
        '                                       <option value="*=" class="hide">取参数值</option>' +
        '                                   </select>' +
        '                               </li>' +
        '                               <li class="col-xs-4">' +
        '                                   <input class="form-control pointCustomVal screen_term" type="text" value="" placeholder="输入值" style="width:100%">' +
        '                               </li>' +
        '                           </ul>' +
        '                       </li>' +
        '                       <li class="col-xs-12 screen_logic_item">' +
        '                           <ul class="list-inline text-center">' +
        '                               <li class="col-xs-4">' +
        '                                   <select class="selectpicker pointScreenList screen_term" title="选择筛选字段" data-size="5" data-width="100%">' +
        '                                       <!-- 后台加载筛选字段 -->' +
        '                                   </select>' +
        '                               </li>' +
        '                               <li class="col-xs-4">' +
        '                                   <select class="selectpicker pointOperatorList screen_term" title="选择运算符" data-size="5" data-width="100%">' +
        '                                       <option value=">=">大于等于</option>' +
        '                                       <option value=">">大于</option>' +
        '                                       <option value="=">等于</option>' +
        '                                       <option value="<">小于</option>' +
        '                                       <option value="<=">小于等于</option>' +
        '                                       <option value="contains">包含</option>' +
        '                                       <option value="notcontains">不包含</option>' +
        '                                       <option value="*=" class="hide">取参数值</option>' +
        '                                   </select>' +
        '                               </li>' +
        '                               <li class="col-xs-4">' +
        '                                   <input class="form-control pointCustomVal screen_term" type="text" value="" placeholder="输入值" style="width:100%">' +
        '                               </li>' +
        '                           </ul>' +
        '                       </li>' +
        '                       <li class="col-xs-12 screen_logic_item">' +
        '                           <ul class="list-inline text-center">' +
        '                               <li class="col-xs-4">' +
        '                                   <select class="selectpicker pointScreenList screen_term" title="选择筛选字段" data-size="5" data-width="100%">' +
        '                                       <!-- 后台加载筛选字段 -->' +
        '                                   </select>' +
        '                               </li>' +
        '                               <li class="col-xs-4">' +
        '                                   <select class="selectpicker pointOperatorList screen_term" title="选择运算符" data-size="5" data-width="100%">' +
        '                                       <option value=">=">大于等于</option>' +
        '                                       <option value=">">大于</option>' +
        '                                       <option value="=">等于</option>' +
        '                                       <option value="<">小于</option>' +
        '                                       <option value="<=">小于等于</option>' +
        '                                       <option value="contains">包含</option>' +
        '                                       <option value="notcontains">不包含</option>' +
        '                                       <option value="*=" class="hide">取参数值</option>' +
        '                                   </select>' +
        '                               </li>' +
        '                               <li class="col-xs-4">' +
        '                                   <input class="form-control pointCustomVal screen_term" type="text" value="" placeholder="输入值" style="width:100%">' +
        '                               </li>' +
        '                           </ul>' +
        '                       </li>' +
        '                   </ul>' +
        '               </div>' +
        '               <div class="modal-footer">' +
        '                   <button type="button" class="btn btn-sm btn-default" data-dismiss="modal" id="pointScreenModalCancelBtn">取消</button>' +
        '                   <button type="button" class="btn btn-sm btn-primary" data-dismiss="modal" id="pointScreenModalAffirmBtn">确认</button>' +
        '               </div>' +
        '           </div>' +
        '       </div>' +
        '   </div>' +
        '    </div>' +
        '</div>';
}