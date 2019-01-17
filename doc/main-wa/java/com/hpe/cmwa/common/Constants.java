package com.hpe.cmwa.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hpe.cmwa.util.HelperString;
import com.hpe.cmwa.util.Key_Value;
import com.hpe.cmwa.util.Prvd_info;

public final class Constants {
	

	/**
	 * 代表全国的机构编码
	 */
	public static final int	ChinaCode			= 10000;
	public static final char				PERCENT				= '%';

	// 省份ID排序
	private static final List<Integer>		PROVID_ORDER		= new ArrayList<Integer>();

	private static final List<Prvd_info>	PRVDINFO_LIST		= new ArrayList<Prvd_info>();

	private static final List<Key_Value>	ATTR_NM_CODE_LIST	= new ArrayList<Key_Value>();
	private static final List<Key_Value>	CTY_LIST			= new ArrayList<Key_Value>();

	static {
		initAttrCodeNm();
		initPROVID_ORDER();
		initPRVDINFO_LIST();
		initCTY_LIST();

	}

	public static Key_Value getATTKVByX(String x) {
		if (HelperString.isNullOrEmpty(x)) {
			return null;
		}
		x = x.toUpperCase();
		for (Key_Value kv : ATTR_NM_CODE_LIST) {
			if (kv.isContains(x)) {
				return kv;
			}
		}
		return null;
	}

	public static String getATTKVOtherByX(String x) {
		Key_Value kv = getATTKVByX(x);
		if (kv == null) {
			return null;
		}
		return kv.getk().equals(x) ? kv.getv() : kv.getk();

	}

	public static Key_Value getCTYKVByX(String x) {
		if (HelperString.isNullOrEmpty(x)) {
			return null;
		}
		x = x.toUpperCase();
		for (Key_Value kv : CTY_LIST) {
			if (kv.isContains(x)) {
				return kv;
			}
		}
		return null;
	}
	public static class Concern {

		public static final int	defaultAuditCycleBeforeN	= 1;	// 默认审计周期相对于当前时间的差值，比如1就是默认实现为上个月
		public static final int	defaultAuditInterval		= 1;	// 默认审计周期相对于当前时间的差值，比如1就是默认审计区间持续1个月

	}
	public static class FileType {

		// 审计报告audReport，审计清单
		public static final String	AUD_REPORT			= "audReport";
		public static final String	AUD_DETAIL			= "audDetail";
		
		public static final String	AUD_ZIP				= "audZip";
		
		
		public static final String	AUD_REPORT_MESSAGE	= "审计报告";
		public static final String	AUD_DETAIL_MESSAGE	= "审计清单";

		public static String getDesc(String key) {

			if (AUD_REPORT.equalsIgnoreCase(key)) {
				return AUD_REPORT_MESSAGE;
			}
			if (AUD_DETAIL.equalsIgnoreCase(key)) {
				return AUD_DETAIL_MESSAGE;
			}

			return "其他";

		}
	}

	public static String getCTYKVOtherByX(String x) {
		Key_Value kv = getCTYKVByX(x);
		if (kv == null) {
			return null;
		}
		return kv.getk().equals(x) ? kv.getv() : kv.getk();

	}

	public static Prvd_info getPrvdInfo(String x) {
		if (HelperString.isNullOrEmpty(x)) {
			return null;
		}
		x = x.toUpperCase();
		for (Prvd_info p : PRVDINFO_LIST) {
			if (p.containsInfo(x)) {
				return p;
			}
		}
		return null;
	}

	public static List<Prvd_info> getPrvdInfoList() {
		return PRVDINFO_LIST;
	}

	public static String getPrvdNmByX(String str) {
		Prvd_info p = getPrvdInfo(str);
		if (p == null) {
			return null;
		}
		return p.getPrivdnm();
	}

	public static String getPrvdCdByX(String str) {
		Prvd_info p = getPrvdInfo(str);
		if (p == null) {
			return null;
		}
		return p.getPrivdcd();
	}

	public static String getPrvdIdByX(String str) {
		Prvd_info p = getPrvdInfo(str);
		if (p == null) {
			return null;
		}
		return p.getPrvdid();
	}

	private static void initPROVID_ORDER() {
		PROVID_ORDER.add(10100);
		PROVID_ORDER.add(10200);
		PROVID_ORDER.add(10300);
		PROVID_ORDER.add(10400);
		PROVID_ORDER.add(10500);
		PROVID_ORDER.add(10600);
		PROVID_ORDER.add(10700);
		PROVID_ORDER.add(10800);
		PROVID_ORDER.add(10900);
		PROVID_ORDER.add(11000);
		PROVID_ORDER.add(11100);
		PROVID_ORDER.add(11200);
		PROVID_ORDER.add(11300);
		PROVID_ORDER.add(11400);
		PROVID_ORDER.add(11500);
		PROVID_ORDER.add(11600);
		PROVID_ORDER.add(11700);
		PROVID_ORDER.add(11800);
		PROVID_ORDER.add(11900);
		PROVID_ORDER.add(12000);
		PROVID_ORDER.add(12100);
		PROVID_ORDER.add(12200);
		PROVID_ORDER.add(12300);
		PROVID_ORDER.add(12400);
		PROVID_ORDER.add(12500);
		PROVID_ORDER.add(12600);
		PROVID_ORDER.add(12700);
		PROVID_ORDER.add(12800);
		PROVID_ORDER.add(12900);
		PROVID_ORDER.add(13000);
		PROVID_ORDER.add(13100);
	}

	private static void initPRVDINFO_LIST() {
		PRVDINFO_LIST.add(new Prvd_info("10000", "QUANGUO", "全公司"));
		PRVDINFO_LIST.add(new Prvd_info("10100", "BEIJING", "北京"));
		PRVDINFO_LIST.add(new Prvd_info("10200", "SHANGHAI", "上海"));
		PRVDINFO_LIST.add(new Prvd_info("10300", "TIANJIN", "天津"));
		PRVDINFO_LIST.add(new Prvd_info("10400", "CHONGQING", "重庆"));
		PRVDINFO_LIST.add(new Prvd_info("10500", "GUIZHOU", "贵州"));
		PRVDINFO_LIST.add(new Prvd_info("10600", "HUBEI", "湖北"));
		PRVDINFO_LIST.add(new Prvd_info("10700", "SHANXI", "陕西"));
		PRVDINFO_LIST.add(new Prvd_info("10800", "HEBEI", "河北"));
		PRVDINFO_LIST.add(new Prvd_info("10900", "HENAN", "河南"));
		PRVDINFO_LIST.add(new Prvd_info("11000", "ANHUI", "安徽"));
		PRVDINFO_LIST.add(new Prvd_info("11100", "FUJIAN", "福建"));
		PRVDINFO_LIST.add(new Prvd_info("11200", "QINGHAI", "青海"));
		PRVDINFO_LIST.add(new Prvd_info("11300", "GANSU", "甘肃"));
		PRVDINFO_LIST.add(new Prvd_info("11400", "ZHEJIANG", "浙江"));
		PRVDINFO_LIST.add(new Prvd_info("11500", "HAINAN", "海南"));
		PRVDINFO_LIST.add(new Prvd_info("11600", "HEILONGJIANG", "黑龙江"));
		PRVDINFO_LIST.add(new Prvd_info("11700", "JIANGSU", "江苏"));
		PRVDINFO_LIST.add(new Prvd_info("11800", "JILIN", "吉林"));
		PRVDINFO_LIST.add(new Prvd_info("11900", "NINGXIA", "宁夏"));
		PRVDINFO_LIST.add(new Prvd_info("12000", "SHANDONG", "山东"));
		PRVDINFO_LIST.add(new Prvd_info("12100", "SHANXI2", "山西"));
		PRVDINFO_LIST.add(new Prvd_info("12200", "XINJIANG", "新疆"));
		PRVDINFO_LIST.add(new Prvd_info("12300", "GUANGDONG", "广东"));
		PRVDINFO_LIST.add(new Prvd_info("12400", "LIAONING", "辽宁"));
		PRVDINFO_LIST.add(new Prvd_info("12500", "GUANGXI", "广西"));
		PRVDINFO_LIST.add(new Prvd_info("12600", "HUNAN", "湖南"));
		PRVDINFO_LIST.add(new Prvd_info("12700", "JIANGXI", "江西"));
		PRVDINFO_LIST.add(new Prvd_info("12800", "NEIMENGGU", "内蒙古"));
		PRVDINFO_LIST.add(new Prvd_info("12900", "YUNNAN", "云南"));
		PRVDINFO_LIST.add(new Prvd_info("13000", "SICHUAN", "四川"));
		PRVDINFO_LIST.add(new Prvd_info("13100", "XIZANG", "西藏"));

	}

	public static void initAttrCodeNm() {
		ATTR_NM_CODE_LIST.add(new Key_Value("ATTR_CODE", "ATTR_NAME"));
		ATTR_NM_CODE_LIST.add(new Key_Value("NUM", "数量"));
		ATTR_NM_CODE_LIST.add(new Key_Value("AMT", "金额"));
		ATTR_NM_CODE_LIST.add(new Key_Value("PER", "占比"));
		ATTR_NM_CODE_LIST.add(new Key_Value("YOY", "同比"));
		ATTR_NM_CODE_LIST.add(new Key_Value("MOM", "环比"));
		ATTR_NM_CODE_LIST.add(new Key_Value("TOL", "总"));
		ATTR_NM_CODE_LIST.add(new Key_Value("TOL_AMT", "总金额"));
		ATTR_NM_CODE_LIST.add(new Key_Value("TOL_NUM", "总数量"));
		ATTR_NM_CODE_LIST.add(new Key_Value("ALARM_STATUS", "总数量"));

	}

	public static List<Integer> getPROVID_ORDER() {
		return PROVID_ORDER;
	}

	private static void initCTY_LIST() {
		CTY_LIST.add(new Key_Value("六安市", "liuan"));
		CTY_LIST.add(new Key_Value("安庆市", "anqing"));
		CTY_LIST.add(new Key_Value("滁州市", "chuzhou"));
		CTY_LIST.add(new Key_Value("宣城市", "xuancheng"));
		CTY_LIST.add(new Key_Value("阜阳市", "fuyang"));
		CTY_LIST.add(new Key_Value("宿州市", "suzhou"));
		CTY_LIST.add(new Key_Value("黄山市", "huangshan"));
		CTY_LIST.add(new Key_Value("巢湖市", "chaohu"));
		CTY_LIST.add(new Key_Value("亳州市", "bozhou"));
		CTY_LIST.add(new Key_Value("池州市", "chizhou"));
		CTY_LIST.add(new Key_Value("合肥市", "hefei"));
		CTY_LIST.add(new Key_Value("蚌埠市", "bengbu"));
		CTY_LIST.add(new Key_Value("芜湖市", "wuhu"));
		CTY_LIST.add(new Key_Value("淮北市", "huaibei"));
		CTY_LIST.add(new Key_Value("淮南市", "huainan"));
		CTY_LIST.add(new Key_Value("马鞍山市", "maanshan"));
		CTY_LIST.add(new Key_Value("铜陵市", "tongling"));
		CTY_LIST.add(new Key_Value("澳门", "aomen"));
		CTY_LIST.add(new Key_Value("密云县", "miyunxian"));
		CTY_LIST.add(new Key_Value("怀柔区", "huairouqu"));
		CTY_LIST.add(new Key_Value("房山区", "fangshanqu"));
		CTY_LIST.add(new Key_Value("延庆县", "yanqingxian"));
		CTY_LIST.add(new Key_Value("门头沟区", "mentougouqu"));
		CTY_LIST.add(new Key_Value("昌平区", "changpingqu"));
		CTY_LIST.add(new Key_Value("大兴区", "daxingqu"));
		CTY_LIST.add(new Key_Value("顺义区", "shunyiqu"));
		CTY_LIST.add(new Key_Value("平谷区", "pingguqu"));
		CTY_LIST.add(new Key_Value("通州区", "tongzhouqu"));
		CTY_LIST.add(new Key_Value("朝阳区", "chaoyangqu"));
		CTY_LIST.add(new Key_Value("海淀区", "haidianqu"));
		CTY_LIST.add(new Key_Value("丰台区", "fengtaiqu"));
		CTY_LIST.add(new Key_Value("石景山区", "shijingshanqu"));
		CTY_LIST.add(new Key_Value("西城区", "xichengqu"));
		CTY_LIST.add(new Key_Value("东城区", "dongchengqu"));
		CTY_LIST.add(new Key_Value("宣武区", "xuanwuqu"));
		CTY_LIST.add(new Key_Value("崇文区", "chongwenqu"));
		CTY_LIST.add(new Key_Value("酉阳", "youyang"));
		CTY_LIST.add(new Key_Value("奉节县", "fengjiexian"));
		CTY_LIST.add(new Key_Value("巫溪县", "wuxixian"));
		CTY_LIST.add(new Key_Value("开县", "kaixian"));
		CTY_LIST.add(new Key_Value("彭水", "pengshui"));
		CTY_LIST.add(new Key_Value("云阳县", "yunyangxian"));
		CTY_LIST.add(new Key_Value("万州区", "wanzhouqu"));
		CTY_LIST.add(new Key_Value("城口县", "chengkouxian"));
		CTY_LIST.add(new Key_Value("江津区", "jiangjinqu"));
		CTY_LIST.add(new Key_Value("石柱", "shizhu"));
		CTY_LIST.add(new Key_Value("巫山县", "wushanxian"));
		CTY_LIST.add(new Key_Value("涪陵区", "fulingqu"));
		CTY_LIST.add(new Key_Value("丰都县", "fengdouxian"));
		CTY_LIST.add(new Key_Value("武隆县", "wulongxian"));
		CTY_LIST.add(new Key_Value("南川区", "nanchuanqu"));
		CTY_LIST.add(new Key_Value("秀山", "xiushan"));
		CTY_LIST.add(new Key_Value("黔江区", "qianjiangqu"));
		CTY_LIST.add(new Key_Value("合川区", "hechuanqu"));
		CTY_LIST.add(new Key_Value("綦江县", "qijiangxian"));
		CTY_LIST.add(new Key_Value("忠县", "zhongxian"));
		CTY_LIST.add(new Key_Value("梁平县", "liangpingxian"));
		CTY_LIST.add(new Key_Value("巴南区", "bananqu"));
		CTY_LIST.add(new Key_Value("潼南县", "tongnanxian"));
		CTY_LIST.add(new Key_Value("永川区", "yongchuanqu"));
		CTY_LIST.add(new Key_Value("垫江县", "dianjiangxian"));
		CTY_LIST.add(new Key_Value("渝北区", "yubeiqu"));
		CTY_LIST.add(new Key_Value("长寿区", "changshouqu"));
		CTY_LIST.add(new Key_Value("大足县", "dazuxian"));
		CTY_LIST.add(new Key_Value("铜梁县", "tongliangxian"));
		CTY_LIST.add(new Key_Value("荣昌县", "rongchangxian"));
		CTY_LIST.add(new Key_Value("璧山县", "bishanxian"));
		CTY_LIST.add(new Key_Value("北碚区", "beibeiqu"));
		CTY_LIST.add(new Key_Value("万盛区", "wanshengqu"));
		CTY_LIST.add(new Key_Value("九龙坡区", "jiulongpoqu"));
		CTY_LIST.add(new Key_Value("沙坪坝区", "shapingbaqu"));
		CTY_LIST.add(new Key_Value("南岸区", "nananqu"));
		CTY_LIST.add(new Key_Value("江北区", "jiangbeiqu"));
		CTY_LIST.add(new Key_Value("大渡口区", "dadukouqu"));
		CTY_LIST.add(new Key_Value("双桥区", "shuangqiaoqu"));
		CTY_LIST.add(new Key_Value("渝中区", "yuzhongqu"));
		CTY_LIST.add(new Key_Value("南平市", "nanping"));
		CTY_LIST.add(new Key_Value("三明市", "sanming"));
		CTY_LIST.add(new Key_Value("龙岩市", "longyan"));
		CTY_LIST.add(new Key_Value("宁德市", "ningde"));
		CTY_LIST.add(new Key_Value("福州市", "fuzhou"));
		CTY_LIST.add(new Key_Value("漳州市", "zhangzhou"));
		CTY_LIST.add(new Key_Value("泉州市", "quanzhou"));
		CTY_LIST.add(new Key_Value("莆田市", "putian"));
		CTY_LIST.add(new Key_Value("厦门市", "xiamen"));
		CTY_LIST.add(new Key_Value("酒泉市", "jiuquan"));
		CTY_LIST.add(new Key_Value("张掖市", "zhangye"));
		CTY_LIST.add(new Key_Value("甘南藏族自治州", "gannan"));
		CTY_LIST.add(new Key_Value("武威市", "wuwei"));
		CTY_LIST.add(new Key_Value("陇南市", "longnan"));
		CTY_LIST.add(new Key_Value("庆阳市", "qingyang"));
		CTY_LIST.add(new Key_Value("白银市", "baiyin"));
		CTY_LIST.add(new Key_Value("定西市", "dingxi"));
		CTY_LIST.add(new Key_Value("天水市", "tianshui"));
		CTY_LIST.add(new Key_Value("兰州市", "lanzhou"));
		CTY_LIST.add(new Key_Value("平凉市", "pingliang"));
		CTY_LIST.add(new Key_Value("临夏回族自治州", "linxia"));
		CTY_LIST.add(new Key_Value("金昌市", "jinchang"));
		CTY_LIST.add(new Key_Value("嘉峪关市", "jiayuguan"));
		CTY_LIST.add(new Key_Value("清远市", "qingyuan"));
		CTY_LIST.add(new Key_Value("韶关市", "shaoguan"));
		CTY_LIST.add(new Key_Value("湛江市", "zhanjiang"));
		CTY_LIST.add(new Key_Value("梅州市", "meizhou"));
		CTY_LIST.add(new Key_Value("河源市", "heyuan"));
		CTY_LIST.add(new Key_Value("肇庆市", "zhaoqing"));
		CTY_LIST.add(new Key_Value("惠州市", "huizhou"));
		CTY_LIST.add(new Key_Value("茂名市", "maoming"));
		CTY_LIST.add(new Key_Value("江门市", "jiangmen"));
		CTY_LIST.add(new Key_Value("阳江市", "yangjiang"));
		CTY_LIST.add(new Key_Value("云浮市", "yunfu"));
		CTY_LIST.add(new Key_Value("广州市", "guangzhou"));
		CTY_LIST.add(new Key_Value("汕尾市", "shanwei"));
		CTY_LIST.add(new Key_Value("揭阳市", "jieyang"));
		CTY_LIST.add(new Key_Value("珠海市", "zhuhai"));
		CTY_LIST.add(new Key_Value("佛山市", "foshan"));
		CTY_LIST.add(new Key_Value("潮州市", "chaozhou"));
		CTY_LIST.add(new Key_Value("汕头市", "shantou"));
		CTY_LIST.add(new Key_Value("深圳市", "shenzhen"));
		CTY_LIST.add(new Key_Value("东莞市", "dongguan"));
		CTY_LIST.add(new Key_Value("中山市", "zhongshan"));
		CTY_LIST.add(new Key_Value("百色市", "baise"));
		CTY_LIST.add(new Key_Value("河池市", "hechi"));
		CTY_LIST.add(new Key_Value("桂林市", "guilin"));
		CTY_LIST.add(new Key_Value("南宁市", "nanning"));
		CTY_LIST.add(new Key_Value("柳州市", "liuzhou"));
		CTY_LIST.add(new Key_Value("崇左市", "chongzuo"));
		CTY_LIST.add(new Key_Value("来宾市", "laibin"));
		CTY_LIST.add(new Key_Value("玉林市", "yulin"));
		CTY_LIST.add(new Key_Value("梧州市", "wuzhou"));
		CTY_LIST.add(new Key_Value("贺州市", "hezhou"));
		CTY_LIST.add(new Key_Value("钦州市", "qinzhou"));
		CTY_LIST.add(new Key_Value("贵港市", "guigang"));
		CTY_LIST.add(new Key_Value("防城港市", "fangchenggang"));
		CTY_LIST.add(new Key_Value("北海市", "beihai"));
		CTY_LIST.add(new Key_Value("遵义市", "zunyi"));
		CTY_LIST.add(new Key_Value("黔东南苗族侗族自治州", "qiandongnanmiaozudongzuzizhizhou"));
		CTY_LIST.add(new Key_Value("毕节地区", "bijiediqu"));
		CTY_LIST.add(new Key_Value("黔南布依族苗族自治州", "qiannanbuyizumiaozuzizhizhou"));
		CTY_LIST.add(new Key_Value("铜仁地区", "tongrendiqu"));
		CTY_LIST.add(new Key_Value("黔西南布依族苗族自治州", "qianxinanbuyizumiaozuzizhizhou"));
		CTY_LIST.add(new Key_Value("六盘水市", "liupanshui"));
		CTY_LIST.add(new Key_Value("安顺市", "anshun"));
		CTY_LIST.add(new Key_Value("贵阳市", "guiyang"));
		CTY_LIST.add(new Key_Value("儋州市", "danzhou"));
		CTY_LIST.add(new Key_Value("文昌市", "wenchang"));
		CTY_LIST.add(new Key_Value("乐东", "ledonglizuzizhixian"));
		CTY_LIST.add(new Key_Value("三亚市", "sanya"));
		CTY_LIST.add(new Key_Value("琼中", "qiongzhonglizumiaozuzizhixian"));
		CTY_LIST.add(new Key_Value("东方市", "dongfang"));
		CTY_LIST.add(new Key_Value("海口市", "haikou"));
		CTY_LIST.add(new Key_Value("万宁市", "wanning"));
		CTY_LIST.add(new Key_Value("澄迈县", "chengmaixian"));
		CTY_LIST.add(new Key_Value("白沙", "baishalizuzizhixian"));
		CTY_LIST.add(new Key_Value("琼海市", "qionghai"));
		CTY_LIST.add(new Key_Value("昌江", "changjianglizuzizhixian"));
		CTY_LIST.add(new Key_Value("临高县", "lingaoxian"));
		CTY_LIST.add(new Key_Value("陵水", "lingshuilizuzizhixian"));
		CTY_LIST.add(new Key_Value("屯昌县", "tunchangxian"));
		CTY_LIST.add(new Key_Value("定安县", "dinganxian"));
		CTY_LIST.add(new Key_Value("保亭", "baotinglizumiaozuzizhixian"));
		CTY_LIST.add(new Key_Value("五指山市", "wuzhishan"));
		CTY_LIST.add(new Key_Value("承德市", "chengde"));
		CTY_LIST.add(new Key_Value("张家口市", "zhangjiakou"));
		CTY_LIST.add(new Key_Value("保定市", "baoding"));
		CTY_LIST.add(new Key_Value("唐山市", "tangshan"));
		CTY_LIST.add(new Key_Value("沧州市", "cangzhou"));
		CTY_LIST.add(new Key_Value("石家庄市", "shijiazhuang"));
		CTY_LIST.add(new Key_Value("邢台市", "xingtai"));
		CTY_LIST.add(new Key_Value("邯郸市", "handan"));
		CTY_LIST.add(new Key_Value("秦皇岛市", "qinhuangdao"));
		CTY_LIST.add(new Key_Value("衡水市", "hengshui"));
		CTY_LIST.add(new Key_Value("廊坊市", "langfang"));
		CTY_LIST.add(new Key_Value("南阳市", "nanyang"));
		CTY_LIST.add(new Key_Value("信阳市", "xinyang"));
		CTY_LIST.add(new Key_Value("洛阳市", "luoyang"));
		CTY_LIST.add(new Key_Value("驻马店市", "zhumadian"));
		CTY_LIST.add(new Key_Value("周口市", "zhoukou"));
		CTY_LIST.add(new Key_Value("商丘市", "shangqiu"));
		CTY_LIST.add(new Key_Value("三门峡市", "sanmenxia"));
		CTY_LIST.add(new Key_Value("新乡市", "xinxiang"));
		CTY_LIST.add(new Key_Value("平顶山市", "pingdingshan"));
		CTY_LIST.add(new Key_Value("郑州市", "zhengzhou"));
		CTY_LIST.add(new Key_Value("安阳市", "anyang"));
		CTY_LIST.add(new Key_Value("开封市", "kaifeng"));
		CTY_LIST.add(new Key_Value("焦作市", "jiaozuo"));
		CTY_LIST.add(new Key_Value("许昌市", "xuchang"));
		CTY_LIST.add(new Key_Value("濮阳市", "puyang"));
		CTY_LIST.add(new Key_Value("漯河市", "luohe"));
		CTY_LIST.add(new Key_Value("鹤壁市", "hebi"));
		CTY_LIST.add(new Key_Value("黑河市", "heihe"));
		CTY_LIST.add(new Key_Value("大兴安岭", "daxinganlingdiqu"));
		CTY_LIST.add(new Key_Value("哈尔滨市", "haerbin"));
		CTY_LIST.add(new Key_Value("齐齐哈尔市", "qiqihaer"));
		CTY_LIST.add(new Key_Value("牡丹江市", "mudanjiang"));
		CTY_LIST.add(new Key_Value("绥化市", "suihua"));
		CTY_LIST.add(new Key_Value("伊春市", "yichun"));
		CTY_LIST.add(new Key_Value("佳木斯市", "jiamusi"));
		CTY_LIST.add(new Key_Value("鸡西市", "jixi"));
		CTY_LIST.add(new Key_Value("双鸭山市", "shuangyashan"));
		CTY_LIST.add(new Key_Value("大庆市", "daqing"));
		CTY_LIST.add(new Key_Value("鹤岗市", "hegang"));
		CTY_LIST.add(new Key_Value("七台河市", "qitaihe"));
		CTY_LIST.add(new Key_Value("恩施", "enshi"));
		CTY_LIST.add(new Key_Value("十堰市", "shiyan"));
		CTY_LIST.add(new Key_Value("宜昌市", "yichang"));
		CTY_LIST.add(new Key_Value("襄阳市", "xiangyang"));
		CTY_LIST.add(new Key_Value("黄冈市", "huanggang"));
		CTY_LIST.add(new Key_Value("荆州市", "jingzhou"));
		CTY_LIST.add(new Key_Value("荆门市", "jingmen"));
		CTY_LIST.add(new Key_Value("咸宁市", "xianning"));
		CTY_LIST.add(new Key_Value("随州市", "suizhou"));
		CTY_LIST.add(new Key_Value("孝感市", "xiaogan"));
		CTY_LIST.add(new Key_Value("武汉市", "wuhan"));
		CTY_LIST.add(new Key_Value("黄石市", "huangshi"));
		CTY_LIST.add(new Key_Value("神农架林区", "shennongjialinqu"));
		CTY_LIST.add(new Key_Value("天门市", "tianmen"));
		CTY_LIST.add(new Key_Value("仙桃市", "xiantao"));
		CTY_LIST.add(new Key_Value("潜江市", "qianjiang"));
		CTY_LIST.add(new Key_Value("鄂州市", "ezhou"));
		CTY_LIST.add(new Key_Value("怀化市", "huaihua"));
		CTY_LIST.add(new Key_Value("永州市", "yongzhou"));
		CTY_LIST.add(new Key_Value("邵阳市", "shaoyang"));
		CTY_LIST.add(new Key_Value("郴州市", "chenzhou"));
		CTY_LIST.add(new Key_Value("常德市", "changde"));
		CTY_LIST.add(new Key_Value("湘西", "xiangxitujiazumiaozuzizhizhou"));
		CTY_LIST.add(new Key_Value("衡阳市", "hengyang"));
		CTY_LIST.add(new Key_Value("岳阳市", "yueyang"));
		CTY_LIST.add(new Key_Value("益阳市", "yiyang"));
		CTY_LIST.add(new Key_Value("长沙市", "changsha"));
		CTY_LIST.add(new Key_Value("株洲市", "zhuzhou"));
		CTY_LIST.add(new Key_Value("张家界市", "zhangjiajie"));
		CTY_LIST.add(new Key_Value("娄底市", "loudi"));
		CTY_LIST.add(new Key_Value("湘潭市", "xiangtan"));
		CTY_LIST.add(new Key_Value("延边", "yanbianchaoxianzuzizhizhou"));
		CTY_LIST.add(new Key_Value("吉林市", "jilin"));
		CTY_LIST.add(new Key_Value("白城市", "baicheng"));
		CTY_LIST.add(new Key_Value("松原市", "songyuan"));
		CTY_LIST.add(new Key_Value("长春市", "changchun"));
		CTY_LIST.add(new Key_Value("白山市", "baishan"));
		CTY_LIST.add(new Key_Value("通化市", "tonghua"));
		CTY_LIST.add(new Key_Value("四平市", "siping"));
		CTY_LIST.add(new Key_Value("辽源市", "liaoyuan"));
		CTY_LIST.add(new Key_Value("盐城市", "yancheng"));
		CTY_LIST.add(new Key_Value("徐州市", "xuzhou"));
		CTY_LIST.add(new Key_Value("南通市", "nantong"));
		CTY_LIST.add(new Key_Value("淮安市", "huaian"));
		CTY_LIST.add(new Key_Value("苏州市", "suzhou"));
		CTY_LIST.add(new Key_Value("宿迁市", "suqian"));
		CTY_LIST.add(new Key_Value("连云港市", "lianyungang"));
		CTY_LIST.add(new Key_Value("扬州市", "yangzhou"));
		CTY_LIST.add(new Key_Value("南京市", "nanjing"));
		CTY_LIST.add(new Key_Value("泰州市", "taizhou"));
		CTY_LIST.add(new Key_Value("无锡市", "wuxi"));
		CTY_LIST.add(new Key_Value("常州市", "changzhou"));
		CTY_LIST.add(new Key_Value("镇江市", "zhenjiang"));
		CTY_LIST.add(new Key_Value("赣州市", "ganzhou"));
		CTY_LIST.add(new Key_Value("吉安市", "jian"));
		CTY_LIST.add(new Key_Value("上饶市", "shangrao"));
		CTY_LIST.add(new Key_Value("九江市", "jiujiang"));
		CTY_LIST.add(new Key_Value("抚州市", "fuzhou"));
		CTY_LIST.add(new Key_Value("宜春市", "yichun"));
		CTY_LIST.add(new Key_Value("南昌市", "nanchang"));
		CTY_LIST.add(new Key_Value("景德镇市", "jingdezhen"));
		CTY_LIST.add(new Key_Value("萍乡市", "pingxiang"));
		CTY_LIST.add(new Key_Value("鹰潭市", "yingtan"));
		CTY_LIST.add(new Key_Value("新余市", "xinyu"));
		CTY_LIST.add(new Key_Value("大连市", "dalian"));
		CTY_LIST.add(new Key_Value("朝阳市", "zhaoyang"));
		CTY_LIST.add(new Key_Value("丹东市", "dandong"));
		CTY_LIST.add(new Key_Value("铁岭市", "tieling"));
		CTY_LIST.add(new Key_Value("沈阳市", "shenyang"));
		CTY_LIST.add(new Key_Value("抚顺市", "fushun"));
		CTY_LIST.add(new Key_Value("葫芦岛市", "huludao"));
		CTY_LIST.add(new Key_Value("阜新市", "fuxin"));
		CTY_LIST.add(new Key_Value("锦州市", "jinzhou"));
		CTY_LIST.add(new Key_Value("鞍山市", "anshan"));
		CTY_LIST.add(new Key_Value("本溪市", "benxi"));
		CTY_LIST.add(new Key_Value("营口市", "yingkou"));
		CTY_LIST.add(new Key_Value("辽阳市", "liaoyang"));
		CTY_LIST.add(new Key_Value("盘锦市", "panjin"));
		CTY_LIST.add(new Key_Value("呼伦贝尔市", "hulunbeier"));
		CTY_LIST.add(new Key_Value("阿拉善盟", "alashanmeng"));
		CTY_LIST.add(new Key_Value("锡林郭勒盟", "xilinguolemeng"));
		CTY_LIST.add(new Key_Value("鄂尔多斯市", "eerduosi"));
		CTY_LIST.add(new Key_Value("赤峰市", "chifeng"));
		CTY_LIST.add(new Key_Value("巴彦淖尔市", "bayannaoer"));
		CTY_LIST.add(new Key_Value("通辽市", "tongliao"));
		CTY_LIST.add(new Key_Value("乌兰察布市", "wulanchabu"));
		CTY_LIST.add(new Key_Value("兴安盟", "xinganmeng"));
		CTY_LIST.add(new Key_Value("包头市", "baotou"));
		CTY_LIST.add(new Key_Value("呼和浩特市", "huhehaote"));
		CTY_LIST.add(new Key_Value("乌海市", "wuhai"));
		CTY_LIST.add(new Key_Value("吴忠市", "wuzhong"));
		CTY_LIST.add(new Key_Value("中卫市", "zhongwei"));
		CTY_LIST.add(new Key_Value("固原市", "guyuan"));
		CTY_LIST.add(new Key_Value("银川市", "yinchuan"));
		CTY_LIST.add(new Key_Value("石嘴山市", "shizuishan"));
		CTY_LIST.add(new Key_Value("海西", "haiximengguzuzangzuzizhizhou"));
		CTY_LIST.add(new Key_Value("玉树", "yushuzangzuzizhizhou"));
		CTY_LIST.add(new Key_Value("果洛", "guoluozangzuzizhizhou"));
		CTY_LIST.add(new Key_Value("海南", "hainanzangzuzizhizhou"));
		CTY_LIST.add(new Key_Value("海北", "haibeizangzuzizhizhou"));
		CTY_LIST.add(new Key_Value("黄南", "huangnanzangzuzizhizhou"));
		CTY_LIST.add(new Key_Value("海东", "haidongdiqu"));
		CTY_LIST.add(new Key_Value("西宁市", "xining"));
		CTY_LIST.add(new Key_Value("烟台市", "yantai"));
		CTY_LIST.add(new Key_Value("临沂市", "linyi"));
		CTY_LIST.add(new Key_Value("潍坊市", "weifang"));
		CTY_LIST.add(new Key_Value("青岛市", "qingdao"));
		CTY_LIST.add(new Key_Value("菏泽市", "heze"));
		CTY_LIST.add(new Key_Value("济宁市", "jining"));
		CTY_LIST.add(new Key_Value("德州市", "dezhou"));
		CTY_LIST.add(new Key_Value("滨州市", "binzhou"));
		CTY_LIST.add(new Key_Value("聊城市", "liaocheng"));
		CTY_LIST.add(new Key_Value("东营市", "dongying"));
		CTY_LIST.add(new Key_Value("济南市", "jinan"));
		CTY_LIST.add(new Key_Value("泰安市", "taian"));
		CTY_LIST.add(new Key_Value("威海市", "weihai"));
		CTY_LIST.add(new Key_Value("日照市", "rizhao"));
		CTY_LIST.add(new Key_Value("淄博市", "zibo"));
		CTY_LIST.add(new Key_Value("枣庄市", "zaozhuang"));
		CTY_LIST.add(new Key_Value("莱芜市", "laiwu"));
		CTY_LIST.add(new Key_Value("榆林市", "yulin"));
		CTY_LIST.add(new Key_Value("延安市", "yanan"));
		CTY_LIST.add(new Key_Value("汉中市", "hanzhong"));
		CTY_LIST.add(new Key_Value("安康市", "ankang"));
		CTY_LIST.add(new Key_Value("商洛市", "shangluo"));
		CTY_LIST.add(new Key_Value("宝鸡市", "baoji"));
		CTY_LIST.add(new Key_Value("渭南市", "weinan"));
		CTY_LIST.add(new Key_Value("咸阳市", "xianyang"));
		CTY_LIST.add(new Key_Value("西安市", "xian"));
		CTY_LIST.add(new Key_Value("铜川市", "tongchuan"));
		CTY_LIST.add(new Key_Value("忻州市", "xinzhou"));
		CTY_LIST.add(new Key_Value("吕梁市", "lvliang"));
		CTY_LIST.add(new Key_Value("临汾市", "linfen"));
		CTY_LIST.add(new Key_Value("晋中市", "jinzhong"));
		CTY_LIST.add(new Key_Value("运城市", "yuncheng"));
		CTY_LIST.add(new Key_Value("大同市", "datong"));
		CTY_LIST.add(new Key_Value("长治市", "changzhi"));
		CTY_LIST.add(new Key_Value("朔州市", "shuozhou"));
		CTY_LIST.add(new Key_Value("晋城市", "jincheng"));
		CTY_LIST.add(new Key_Value("太原市", "taiyuan"));
		CTY_LIST.add(new Key_Value("阳泉市", "yangquan"));
		CTY_LIST.add(new Key_Value("崇明县", "chongmingxian"));
		CTY_LIST.add(new Key_Value("南汇区", "nanhuiqu"));
		CTY_LIST.add(new Key_Value("奉贤区", "fengxianqu"));
		CTY_LIST.add(new Key_Value("浦东新区", "pudongxinqu"));
		CTY_LIST.add(new Key_Value("金山区", "jinshanqu"));
		CTY_LIST.add(new Key_Value("青浦区", "qingpuqu"));
		CTY_LIST.add(new Key_Value("松江区", "songjiangqu"));
		CTY_LIST.add(new Key_Value("嘉定区", "jiadingqu"));
		CTY_LIST.add(new Key_Value("宝山区", "baoshanqu"));
		CTY_LIST.add(new Key_Value("闵行区", "minxingqu"));
		CTY_LIST.add(new Key_Value("杨浦区", "yangpuqu"));
		CTY_LIST.add(new Key_Value("普陀区", "putuoqu"));
		CTY_LIST.add(new Key_Value("徐汇区", "xuhuiqu"));
		CTY_LIST.add(new Key_Value("长宁区", "changningqu"));
		CTY_LIST.add(new Key_Value("闸北区", "zhabeiqu"));
		CTY_LIST.add(new Key_Value("虹口区", "hongkouqu"));
		CTY_LIST.add(new Key_Value("黄浦区", "huangpuqu"));
		CTY_LIST.add(new Key_Value("卢湾区", "luwanqu"));
		CTY_LIST.add(new Key_Value("静安区", "jinganqu"));
		CTY_LIST.add(new Key_Value("甘孜", "ganzizangzuzizhizhou"));
		CTY_LIST.add(new Key_Value("阿坝", "abazangzuqiangzuzizhizhou"));
		CTY_LIST.add(new Key_Value("凉山", "liangshanyizuzizhizhou"));
		CTY_LIST.add(new Key_Value("绵阳市", "mianyang"));
		CTY_LIST.add(new Key_Value("达州市", "dazhou"));
		CTY_LIST.add(new Key_Value("广元市", "guangyuan"));
		CTY_LIST.add(new Key_Value("雅安市", "yaan"));
		CTY_LIST.add(new Key_Value("宜宾市", "yibin"));
		CTY_LIST.add(new Key_Value("乐山市", "leshan"));
		CTY_LIST.add(new Key_Value("南充市", "nanchong"));
		CTY_LIST.add(new Key_Value("巴中市", "bazhong"));
		CTY_LIST.add(new Key_Value("泸州市", "luzhou"));
		CTY_LIST.add(new Key_Value("成都市", "chengdu"));
		CTY_LIST.add(new Key_Value("资阳市", "ziyang"));
		CTY_LIST.add(new Key_Value("攀枝花市", "panzhihua"));
		CTY_LIST.add(new Key_Value("眉山市", "meishan"));
		CTY_LIST.add(new Key_Value("广安市", "guangan"));
		CTY_LIST.add(new Key_Value("德阳市", "deyang"));
		CTY_LIST.add(new Key_Value("内江市", "neijiang"));
		CTY_LIST.add(new Key_Value("遂宁市", "suining"));
		CTY_LIST.add(new Key_Value("自贡市", "zigong"));
		CTY_LIST.add(new Key_Value("台湾", "taiwan"));
		CTY_LIST.add(new Key_Value("蓟县", "jixian"));
		CTY_LIST.add(new Key_Value("武清区", "wuqingqu"));
		CTY_LIST.add(new Key_Value("宝坻区", "baodiqu"));
		CTY_LIST.add(new Key_Value("静海县", "jinghaixian"));
		CTY_LIST.add(new Key_Value("宁河县", "ninghexian"));
		CTY_LIST.add(new Key_Value("大港区", "dagangqu"));
		CTY_LIST.add(new Key_Value("塘沽区", "tangguqu"));
		CTY_LIST.add(new Key_Value("西青区", "xiqingqu"));
		CTY_LIST.add(new Key_Value("北辰区", "beichenqu"));
		CTY_LIST.add(new Key_Value("东丽区", "dongliqu"));
		CTY_LIST.add(new Key_Value("汉沽区", "hanguqu"));
		CTY_LIST.add(new Key_Value("津南区", "jinnanqu"));
		CTY_LIST.add(new Key_Value("河西区", "hexiqu"));
		CTY_LIST.add(new Key_Value("河东区", "hedongqu"));
		CTY_LIST.add(new Key_Value("南开区", "nankaiqu"));
		CTY_LIST.add(new Key_Value("河北区", "hebeiqu"));
		CTY_LIST.add(new Key_Value("红桥区", "hongqiaoqu"));
		CTY_LIST.add(new Key_Value("和平区", "hepingqu"));
		CTY_LIST.add(new Key_Value("那曲地区", "naqudiqu"));
		CTY_LIST.add(new Key_Value("阿里地区", "alidiqu"));
		CTY_LIST.add(new Key_Value("日喀则地区", "rikazediqu"));
		CTY_LIST.add(new Key_Value("林芝地区", "linzhidiqu"));
		CTY_LIST.add(new Key_Value("昌都地区", "changdoudiqu"));
		CTY_LIST.add(new Key_Value("山南地区", "shannandiqu"));
		CTY_LIST.add(new Key_Value("拉萨市", "lasa"));
		CTY_LIST.add(new Key_Value("香港", "xianggang"));
		CTY_LIST.add(new Key_Value("巴音郭楞蒙古自治州", "bayinguolengmengguzizhizhou"));
		CTY_LIST.add(new Key_Value("和田地区", "hetiandiqu"));
		CTY_LIST.add(new Key_Value("哈密地区", "hamidiqu"));
		CTY_LIST.add(new Key_Value("阿克苏地区", "akesudiqu"));
		CTY_LIST.add(new Key_Value("阿勒泰地区", "aletaidiqu"));
		CTY_LIST.add(new Key_Value("喀什地区", "kashidiqu"));
		CTY_LIST.add(new Key_Value("塔城地区", "tachengdiqu"));
		CTY_LIST.add(new Key_Value("昌吉回族自治州", "changjihuizuzizhizhou"));
		CTY_LIST.add(new Key_Value("克孜勒苏柯尔克孜自治州", "kezilesukeerkezizizhizhou"));
		CTY_LIST.add(new Key_Value("吐鲁番地区", "tulufandiqu"));
		CTY_LIST.add(new Key_Value("伊犁哈萨克自治州", "yilihasakezizhizhou"));
		CTY_LIST.add(new Key_Value("博尔塔拉蒙古自治州", "boertalamengguzizhizhou"));
		CTY_LIST.add(new Key_Value("乌鲁木齐市", "wulumuqi"));
		CTY_LIST.add(new Key_Value("克拉玛依市", "kelamayi"));
		CTY_LIST.add(new Key_Value("阿拉尔市", "alaer"));
		CTY_LIST.add(new Key_Value("图木舒克市", "tumushuke"));
		CTY_LIST.add(new Key_Value("五家渠市", "wujiaqu"));
		CTY_LIST.add(new Key_Value("石河子市", "shihezi"));
		CTY_LIST.add(new Key_Value("普洱市", "puer"));
		CTY_LIST.add(new Key_Value("红河哈尼族彝族自治州", "honghehanizuyizuzizhizhou"));
		CTY_LIST.add(new Key_Value("文山壮族苗族自治州", "wenshanzhuangzumiaozuzizhizhou"));
		CTY_LIST.add(new Key_Value("曲靖市", "qujing"));
		CTY_LIST.add(new Key_Value("楚雄彝族自治州", "chuxiongyizuzizhizhou"));
		CTY_LIST.add(new Key_Value("大理白族自治州", "dalibaizuzizhizhou"));
		CTY_LIST.add(new Key_Value("临沧市", "lincang"));
		CTY_LIST.add(new Key_Value("迪庆藏族自治州", "diqingzangzuzizhizhou"));
		CTY_LIST.add(new Key_Value("昭通市", "zhaotong"));
		CTY_LIST.add(new Key_Value("昆明市", "kunming"));
		CTY_LIST.add(new Key_Value("丽江市", "lijiang"));
		CTY_LIST.add(new Key_Value("西双版纳傣族自治州", "xishuangbannadaizuzizhizhou"));
		CTY_LIST.add(new Key_Value("保山市", "baoshan"));
		CTY_LIST.add(new Key_Value("玉溪市", "yuxi"));
		CTY_LIST.add(new Key_Value("怒江傈僳族自治州", "nujianglisuzuzizhizhou"));
		CTY_LIST.add(new Key_Value("德宏傣族景颇族自治州", "dehongdaizujingpozuzizhizhou"));
		CTY_LIST.add(new Key_Value("丽水市", "lishui"));
		CTY_LIST.add(new Key_Value("杭州市", "hangzhou"));
		CTY_LIST.add(new Key_Value("温州市", "wenzhou"));
		CTY_LIST.add(new Key_Value("宁波市", "ningbo"));
		CTY_LIST.add(new Key_Value("舟山市", "zhoushan"));
		CTY_LIST.add(new Key_Value("台州市", "taizhou"));
		CTY_LIST.add(new Key_Value("金华市", "jinhua"));
		CTY_LIST.add(new Key_Value("衢州市", "quzhou"));
		CTY_LIST.add(new Key_Value("绍兴市", "shaoxing"));
		CTY_LIST.add(new Key_Value("嘉兴市", "jiaxing"));
		CTY_LIST.add(new Key_Value("湖州市", "huzhoushi"));
	}

	public static class Model {

		public static class ModelType {

			public static final int		CHINA				= 0;
			public static final int		PROVINCE			= 1;

			public static final String	CHINA_MESSAGE		= "全国";
			public static final String	PROVINCE_MESSAGE	= "省";

			public static String getDesc(int key) {

				if (CHINA == key) {
					return CHINA_MESSAGE;
				}
				if (PROVINCE == key) {
					return PROVINCE_MESSAGE;
				}
				return "其他";

			}
		}

		public static class FileRequestStatus {

			// 0-模型运行完毕，1-csv文件生成完毕 2-doc文件生成完毕 3-csv文件ftp完毕 4-doc文件ftp完毕 5-处理完毕
			public static final int		MODEL_FINISHED				= 0;
			public static final int		CSV_FILE_FINISHED			= 1;
			public static final int		DOC_FILE_FINISHED			= 2;
			public static final int		CSV_FTP_FINISHED			= 3;
			public static final int		DOC_FTP_FINISHED			= 4;
			public static final int		File_FINISHED				= 5;

			public static final String	MODEL_FINISHED_MESSAGE		= "模型运行完毕";
			public static final String	CSV_FILE_FINISHED_MESSAGE	= "csv文件生成完毕";
			public static final String	DOC_FILE_FINISHED_MESSAGE	= "doc文件生成完毕";
			public static final String	CSV_FTP_FINISHED_MESSAGE	= "csv文件ftp完毕";
			public static final String	DOC_FTP_FINISHED_MESSAGE	= "doc文件ftp完毕";
			public static final String	File_FINISHED_MESSAGE		= "处理完毕";

			public static String getDesc(int key) {

				if (MODEL_FINISHED == key) {
					return MODEL_FINISHED_MESSAGE;
				}
				if (CSV_FILE_FINISHED == key) {
					return CSV_FILE_FINISHED_MESSAGE;
				}
				if (DOC_FILE_FINISHED == key) {
					return DOC_FILE_FINISHED_MESSAGE;
				}
				if (CSV_FTP_FINISHED == key) {
					return CSV_FTP_FINISHED_MESSAGE;
				}
				if (DOC_FTP_FINISHED == key) {
					return DOC_FTP_FINISHED_MESSAGE;
				}
				if (File_FINISHED == key) {
					return File_FINISHED_MESSAGE;
				}
				return "其他";

			}
		}

		public static class FileType {

			// 审计报告audReport，审计清单
			public static final String	AUD_REPORT			= "audReport";
			public static final String	AUD_DETAIL			= "audDetail";
			
			public static final String	AUD_ZIP				= "audZip";
			
			
			public static final String	AUD_REPORT_MESSAGE	= "审计报告";
			public static final String	AUD_DETAIL_MESSAGE	= "审计清单";

			public static String getDesc(String key) {

				if (AUD_REPORT.equalsIgnoreCase(key)) {
					return AUD_REPORT_MESSAGE;
				}
				if (AUD_DETAIL.equalsIgnoreCase(key)) {
					return AUD_DETAIL_MESSAGE;
				}

				return "其他";

			}
		}
	}
	// 省公司ID和名称映射  20161019 add By GuoXY for 审计报告生成job
    public static final Map<Integer, String> MAP_PROVD_NAME = new LinkedHashMap<Integer, String>();
    static {
    	MAP_PROVD_NAME.put(10000,"全公司");
    	MAP_PROVD_NAME.put(10100,"北京");
    	MAP_PROVD_NAME.put(10200,"上海");
    	MAP_PROVD_NAME.put(10300,"天津");
    	MAP_PROVD_NAME.put(10400,"重庆");
    	MAP_PROVD_NAME.put(10500,"贵州");
    	MAP_PROVD_NAME.put(10600,"湖北");
    	MAP_PROVD_NAME.put(10700,"陕西");
    	MAP_PROVD_NAME.put(10800,"河北");
    	MAP_PROVD_NAME.put(10900,"河南");
    	MAP_PROVD_NAME.put(11000,"安徽");
    	MAP_PROVD_NAME.put(11100,"福建");
    	MAP_PROVD_NAME.put(11200,"青海");
    	MAP_PROVD_NAME.put(11300,"甘肃");
    	MAP_PROVD_NAME.put(11400,"浙江");
    	MAP_PROVD_NAME.put(11500,"海南");
    	MAP_PROVD_NAME.put(11600,"黑龙江");
    	MAP_PROVD_NAME.put(11700,"江苏");
    	MAP_PROVD_NAME.put(11800,"吉林");
    	MAP_PROVD_NAME.put(11900,"宁夏");
    	MAP_PROVD_NAME.put(12000,"山东");
    	MAP_PROVD_NAME.put(12100,"山西");
    	MAP_PROVD_NAME.put(12200,"新疆");
    	MAP_PROVD_NAME.put(12300,"广东");
    	MAP_PROVD_NAME.put(12400,"辽宁");
    	MAP_PROVD_NAME.put(12500,"广西");
    	MAP_PROVD_NAME.put(12600,"湖南");
    	MAP_PROVD_NAME.put(12700,"江西");
    	MAP_PROVD_NAME.put(12800,"内蒙古");
    	MAP_PROVD_NAME.put(12900,"云南");
    	MAP_PROVD_NAME.put(13000,"四川");
    	MAP_PROVD_NAME.put(13100,"西藏");
    }
 // 专题ID与名称映射  20161019 add By GuoXY for 审计报告生成job
    public static final Map<String, String> MAP_SUBJECT_NAME = new LinkedHashMap<String, String>();
    static {
    	MAP_SUBJECT_NAME.put("1001","客户欠费");
    	MAP_SUBJECT_NAME.put("2001","社会渠道酬金");
    	MAP_SUBJECT_NAME.put("3001","有价卡赠送");
    }
	 // 关注点ID与名称映射  20161019 add By GuoXY for 审计报告生成job
    public static final Map<String, String> MAP_FOCUSCD_NAME = new LinkedHashMap<String, String>();
    static {
    	MAP_FOCUSCD_NAME.put("1000","汇总情况");
    	
    }
	public static class AuditTask {

		public static class Result {

			public static final String	SUCCESS			= "00";
			public static final String	FAIL			= "01";

			public static final String	SUCCESS_MESSAGE	= "成功";
			public static final String	FAIL_MESSAGE	= "失败";

			public static String getDesc(String key) {

				if (SUCCESS.equalsIgnoreCase(key)) {
					return SUCCESS_MESSAGE;
				}
				if (FAIL.equalsIgnoreCase(key)) {
					return FAIL_MESSAGE;
				}
				return "其他";

			}
		}

	}

}
