package thatline.localup.tourapi.restclient

enum class KorService2Arrange(
    val code: String,
    val description: String,
) {
    TITLE("A", "제목순"),
    MODIFIED("C", "수정일순"),
    CREATED("D", "생성일순"),

    // 대표 이미지가 반드시 있는 정렬
    TITLE_IMAGE("O", "제목순(대표 이미지)"),
    MODIFIED_IMAGE("Q", "수정일순(대표 이미지)"),
    CREATED_IMAGE("R", "생성일순(대표 이미지)");
}
