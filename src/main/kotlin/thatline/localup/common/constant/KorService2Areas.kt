package thatline.localup.common.constant

import thatline.localup.common.constant.dto.KorService2Area


/**
 * 한국관광공사_국문 관광정보 서비스_GW: 지역 코드
 *
 * @see <a href="https://www.data.go.kr/data/15101578/openapi.do">공공데이터포털 API 문서</a>
 */
object KorService2Areas {
    private val korService2Areas = listOf(
        // 서울특별시
        KorService2Area("1", "서울특별시", "1", "강남구"),
        KorService2Area("1", "서울특별시", "2", "강동구"),
        KorService2Area("1", "서울특별시", "3", "강북구"),
        KorService2Area("1", "서울특별시", "4", "강서구"),
        KorService2Area("1", "서울특별시", "5", "관악구"),
        KorService2Area("1", "서울특별시", "6", "광진구"),
        KorService2Area("1", "서울특별시", "7", "구로구"),
        KorService2Area("1", "서울특별시", "8", "금천구"),
        KorService2Area("1", "서울특별시", "9", "노원구"),
        KorService2Area("1", "서울특별시", "10", "도봉구"),
        KorService2Area("1", "서울특별시", "11", "동대문구"),
        KorService2Area("1", "서울특별시", "12", "동작구"),
        KorService2Area("1", "서울특별시", "13", "마포구"),
        KorService2Area("1", "서울특별시", "14", "서대문구"),
        KorService2Area("1", "서울특별시", "15", "서초구"),
        KorService2Area("1", "서울특별시", "16", "성동구"),
        KorService2Area("1", "서울특별시", "17", "성북구"),
        KorService2Area("1", "서울특별시", "18", "송파구"),
        KorService2Area("1", "서울특별시", "19", "양천구"),
        KorService2Area("1", "서울특별시", "20", "영등포구"),
        KorService2Area("1", "서울특별시", "21", "용산구"),
        KorService2Area("1", "서울특별시", "22", "은평구"),
        KorService2Area("1", "서울특별시", "23", "종로구"),
        KorService2Area("1", "서울특별시", "24", "중구"),
        KorService2Area("1", "서울특별시", "25", "중랑구"),

        // 인천광역시
        KorService2Area("2", "인천광역시", "1", "강화군"),
        KorService2Area("2", "인천광역시", "2", "계양구"),
        KorService2Area("2", "인천광역시", "3", "미추홀구"),
        KorService2Area("2", "인천광역시", "4", "남동구"),
        KorService2Area("2", "인천광역시", "5", "동구"),
        KorService2Area("2", "인천광역시", "6", "부평구"),
        KorService2Area("2", "인천광역시", "7", "서구"),
        KorService2Area("2", "인천광역시", "8", "연수구"),
        KorService2Area("2", "인천광역시", "9", "옹진군"),
        KorService2Area("2", "인천광역시", "10", "중구"),

        // 대전광역시
        KorService2Area("3", "대전광역시", "1", "대덕구"),
        KorService2Area("3", "대전광역시", "2", "동구"),
        KorService2Area("3", "대전광역시", "3", "서구"),
        KorService2Area("3", "대전광역시", "4", "유성구"),
        KorService2Area("3", "대전광역시", "5", "중구"),

        // 대구광역시
        KorService2Area("4", "대구광역시", "1", "남구"),
        KorService2Area("4", "대구광역시", "2", "달서구"),
        KorService2Area("4", "대구광역시", "3", "달성군"),
        KorService2Area("4", "대구광역시", "4", "동구"),
        KorService2Area("4", "대구광역시", "5", "북구"),
        KorService2Area("4", "대구광역시", "6", "서구"),
        KorService2Area("4", "대구광역시", "7", "수성구"),
        KorService2Area("4", "대구광역시", "8", "중구"),
        KorService2Area("4", "대구광역시", "9", "군위군"),

        // 광주광역시
        KorService2Area("5", "광주광역시", "1", "광산구"),
        KorService2Area("5", "광주광역시", "2", "남구"),
        KorService2Area("5", "광주광역시", "3", "동구"),
        KorService2Area("5", "광주광역시", "4", "북구"),
        KorService2Area("5", "광주광역시", "5", "서구"),

        // 부산광역시
        KorService2Area("6", "부산광역시", "1", "강서구"),
        KorService2Area("6", "부산광역시", "2", "금정구"),
        KorService2Area("6", "부산광역시", "3", "기장군"),
        KorService2Area("6", "부산광역시", "4", "남구"),
        KorService2Area("6", "부산광역시", "5", "동구"),
        KorService2Area("6", "부산광역시", "6", "동래구"),
        KorService2Area("6", "부산광역시", "7", "부산진구"),
        KorService2Area("6", "부산광역시", "8", "북구"),
        KorService2Area("6", "부산광역시", "9", "사상구"),
        KorService2Area("6", "부산광역시", "10", "사하구"),
        KorService2Area("6", "부산광역시", "11", "서구"),
        KorService2Area("6", "부산광역시", "12", "수영구"),
        KorService2Area("6", "부산광역시", "13", "연제구"),
        KorService2Area("6", "부산광역시", "14", "영도구"),
        KorService2Area("6", "부산광역시", "15", "중구"),
        KorService2Area("6", "부산광역시", "16", "해운대구"),

        // 울산광역시
        KorService2Area("7", "울산광역시", "1", "중구"),
        KorService2Area("7", "울산광역시", "2", "남구"),
        KorService2Area("7", "울산광역시", "3", "동구"),
        KorService2Area("7", "울산광역시", "4", "북구"),
        KorService2Area("7", "울산광역시", "5", "울주군"),

        // 세종특별자치시
        KorService2Area("8", "세종특별자치시", "1", "세종특별자치시"),

        // 경기도
        KorService2Area("31", "경기도", "1", "가평군"),
        KorService2Area("31", "경기도", "2", "고양시"),
        KorService2Area("31", "경기도", "3", "과천시"),
        KorService2Area("31", "경기도", "4", "광명시"),
        KorService2Area("31", "경기도", "5", "광주시"),
        KorService2Area("31", "경기도", "6", "구리시"),
        KorService2Area("31", "경기도", "7", "군포시"),
        KorService2Area("31", "경기도", "8", "김포시"),
        KorService2Area("31", "경기도", "9", "남양주시"),
        KorService2Area("31", "경기도", "10", "동두천시"),
        KorService2Area("31", "경기도", "11", "부천시"),
        KorService2Area("31", "경기도", "12", "성남시"),
        KorService2Area("31", "경기도", "13", "수원시"),
        KorService2Area("31", "경기도", "14", "시흥시"),
        KorService2Area("31", "경기도", "15", "안산시"),
        KorService2Area("31", "경기도", "16", "안성시"),
        KorService2Area("31", "경기도", "17", "안양시"),
        KorService2Area("31", "경기도", "18", "양주시"),
        KorService2Area("31", "경기도", "19", "양평군"),
        KorService2Area("31", "경기도", "20", "여주시"),
        KorService2Area("31", "경기도", "21", "연천군"),
        KorService2Area("31", "경기도", "22", "오산시"),
        KorService2Area("31", "경기도", "23", "용인시"),
        KorService2Area("31", "경기도", "24", "의왕시"),
        KorService2Area("31", "경기도", "25", "의정부시"),
        KorService2Area("31", "경기도", "26", "이천시"),
        KorService2Area("31", "경기도", "27", "파주시"),
        KorService2Area("31", "경기도", "28", "평택시"),
        KorService2Area("31", "경기도", "29", "포천시"),
        KorService2Area("31", "경기도", "30", "하남시"),
        KorService2Area("31", "경기도", "31", "화성시"),

        // 강원특별자치도
        KorService2Area("32", "강원특별자치도", "1", "강릉시"),
        KorService2Area("32", "강원특별자치도", "2", "고성군"),
        KorService2Area("32", "강원특별자치도", "3", "동해시"),
        KorService2Area("32", "강원특별자치도", "4", "삼척시"),
        KorService2Area("32", "강원특별자치도", "5", "속초시"),
        KorService2Area("32", "강원특별자치도", "6", "양구군"),
        KorService2Area("32", "강원특별자치도", "7", "양양군"),
        KorService2Area("32", "강원특별자치도", "8", "영월군"),
        KorService2Area("32", "강원특별자치도", "9", "원주시"),
        KorService2Area("32", "강원특별자치도", "10", "인제군"),
        KorService2Area("32", "강원특별자치도", "11", "정선군"),
        KorService2Area("32", "강원특별자치도", "12", "철원군"),
        KorService2Area("32", "강원특별자치도", "13", "춘천시"),
        KorService2Area("32", "강원특별자치도", "14", "태백시"),
        KorService2Area("32", "강원특별자치도", "15", "평창군"),
        KorService2Area("32", "강원특별자치도", "16", "홍천군"),
        KorService2Area("32", "강원특별자치도", "17", "화천군"),
        KorService2Area("32", "강원특별자치도", "18", "횡성군"),

        // 충청북도
        KorService2Area("33", "충청북도", "1", "괴산군"),
        KorService2Area("33", "충청북도", "2", "단양군"),
        KorService2Area("33", "충청북도", "3", "보은군"),
        KorService2Area("33", "충청북도", "4", "영동군"),
        KorService2Area("33", "충청북도", "5", "옥천군"),
        KorService2Area("33", "충청북도", "6", "음성군"),
        KorService2Area("33", "충청북도", "7", "제천시"),
        KorService2Area("33", "충청북도", "8", "진천군"),
        KorService2Area("33", "충청북도", "9", "청원군"),
        KorService2Area("33", "충청북도", "10", "청주시"),
        KorService2Area("33", "충청북도", "11", "충주시"),
        KorService2Area("33", "충청북도", "12", "증평군"),

        // 충청남도
        KorService2Area("34", "충청남도", "1", "공주시"),
        KorService2Area("34", "충청남도", "2", "금산군"),
        KorService2Area("34", "충청남도", "3", "논산시"),
        KorService2Area("34", "충청남도", "4", "당진시"),
        KorService2Area("34", "충청남도", "5", "보령시"),
        KorService2Area("34", "충청남도", "6", "부여군"),
        KorService2Area("34", "충청남도", "7", "서산시"),
        KorService2Area("34", "충청남도", "8", "서천군"),
        KorService2Area("34", "충청남도", "9", "아산시"),
        KorService2Area("34", "충청남도", "11", "예산군"),
        KorService2Area("34", "충청남도", "12", "천안시"),
        KorService2Area("34", "충청남도", "13", "청양군"),
        KorService2Area("34", "충청남도", "14", "태안군"),
        KorService2Area("34", "충청남도", "15", "홍성군"),
        KorService2Area("34", "충청남도", "16", "계룡시"),

        // 경상북도
        KorService2Area("35", "경상북도", "1", "경산시"),
        KorService2Area("35", "경상북도", "2", "경주시"),
        KorService2Area("35", "경상북도", "3", "고령군"),
        KorService2Area("35", "경상북도", "4", "구미시"),
        KorService2Area("35", "경상북도", "6", "김천시"),
        KorService2Area("35", "경상북도", "7", "문경시"),
        KorService2Area("35", "경상북도", "8", "봉화군"),
        KorService2Area("35", "경상북도", "9", "상주시"),
        KorService2Area("35", "경상북도", "10", "성주군"),
        KorService2Area("35", "경상북도", "11", "안동시"),
        KorService2Area("35", "경상북도", "12", "영덕군"),
        KorService2Area("35", "경상북도", "13", "영양군"),
        KorService2Area("35", "경상북도", "14", "영주시"),
        KorService2Area("35", "경상북도", "15", "영천시"),
        KorService2Area("35", "경상북도", "16", "예천군"),
        KorService2Area("35", "경상북도", "17", "울릉군"),
        KorService2Area("35", "경상북도", "18", "울진군"),
        KorService2Area("35", "경상북도", "19", "의성군"),
        KorService2Area("35", "경상북도", "20", "청도군"),
        KorService2Area("35", "경상북도", "21", "청송군"),
        KorService2Area("35", "경상북도", "22", "칠곡군"),
        KorService2Area("35", "경상북도", "23", "포항시"),

        // 경상남도
        KorService2Area("36", "경상남도", "1", "거제시"),
        KorService2Area("36", "경상남도", "2", "거창군"),
        KorService2Area("36", "경상남도", "3", "고성군"),
        KorService2Area("36", "경상남도", "4", "김해시"),
        KorService2Area("36", "경상남도", "5", "남해군"),
        KorService2Area("36", "경상남도", "6", "마산시"),
        KorService2Area("36", "경상남도", "7", "밀양시"),
        KorService2Area("36", "경상남도", "8", "사천시"),
        KorService2Area("36", "경상남도", "9", "산청군"),
        KorService2Area("36", "경상남도", "10", "양산시"),
        KorService2Area("36", "경상남도", "12", "의령군"),
        KorService2Area("36", "경상남도", "13", "진주시"),
        KorService2Area("36", "경상남도", "14", "진해시"),
        KorService2Area("36", "경상남도", "15", "창녕군"),
        KorService2Area("36", "경상남도", "16", "창원시"),
        KorService2Area("36", "경상남도", "17", "통영시"),
        KorService2Area("36", "경상남도", "18", "하동군"),
        KorService2Area("36", "경상남도", "19", "함안군"),
        KorService2Area("36", "경상남도", "20", "함양군"),
        KorService2Area("36", "경상남도", "21", "합천군"),

        // 전북특별자치도
        KorService2Area("37", "전북특별자치도", "1", "고창군"),
        KorService2Area("37", "전북특별자치도", "2", "군산시"),
        KorService2Area("37", "전북특별자치도", "3", "김제시"),
        KorService2Area("37", "전북특별자치도", "4", "남원시"),
        KorService2Area("37", "전북특별자치도", "5", "무주군"),
        KorService2Area("37", "전북특별자치도", "6", "부안군"),
        KorService2Area("37", "전북특별자치도", "7", "순창군"),
        KorService2Area("37", "전북특별자치도", "8", "완주군"),
        KorService2Area("37", "전북특별자치도", "9", "익산시"),
        KorService2Area("37", "전북특별자치도", "10", "임실군"),
        KorService2Area("37", "전북특별자치도", "11", "장수군"),
        KorService2Area("37", "전북특별자치도", "12", "전주시"),
        KorService2Area("37", "전북특별자치도", "13", "정읍시"),
        KorService2Area("37", "전북특별자치도", "14", "진안군"),

        // 전라남도
        KorService2Area("38", "전라남도", "1", "강진군"),
        KorService2Area("38", "전라남도", "2", "고흥군"),
        KorService2Area("38", "전라남도", "3", "곡성군"),
        KorService2Area("38", "전라남도", "4", "광양시"),
        KorService2Area("38", "전라남도", "5", "구례군"),
        KorService2Area("38", "전라남도", "6", "나주시"),
        KorService2Area("38", "전라남도", "7", "담양군"),
        KorService2Area("38", "전라남도", "8", "목포시"),
        KorService2Area("38", "전라남도", "9", "무안군"),
        KorService2Area("38", "전라남도", "10", "보성군"),
        KorService2Area("38", "전라남도", "11", "순천시"),
        KorService2Area("38", "전라남도", "12", "신안군"),
        KorService2Area("38", "전라남도", "13", "여수시"),
        KorService2Area("38", "전라남도", "16", "영광군"),
        KorService2Area("38", "전라남도", "17", "영암군"),
        KorService2Area("38", "전라남도", "18", "완도군"),
        KorService2Area("38", "전라남도", "19", "장성군"),
        KorService2Area("38", "전라남도", "20", "장흥군"),
        KorService2Area("38", "전라남도", "21", "진도군"),
        KorService2Area("38", "전라남도", "22", "함평군"),
        KorService2Area("38", "전라남도", "23", "해남군"),
        KorService2Area("38", "전라남도", "24", "화순군"),

        // 제주특별자치도
        KorService2Area("39", "제주특별자치도", "1", "남제주군"),
        KorService2Area("39", "제주특별자치도", "2", "북제주군"),
        KorService2Area("39", "제주특별자치도", "3", "서귀포시"),
        KorService2Area("39", "제주특별자치도", "4", "제주시")
    )

    // O(1) 조회를 위한 Map 캐시
    private val areaCodeMap = korService2Areas.groupBy { it.areaCode }
    private val sigunguCodeMap = korService2Areas.associateBy { "${it.areaCode}_${it.sigunguCode}" }

    fun findAllByAreaCode(areaCode: String): List<KorService2Area> {
        return areaCodeMap[areaCode] ?: emptyList()
    }

    fun findByAreaCodeAndSigunguCode(areaCode: String, sigunguCode: String): KorService2Area? {
        return sigunguCodeMap["${areaCode}_${sigunguCode}"]
    }
}
