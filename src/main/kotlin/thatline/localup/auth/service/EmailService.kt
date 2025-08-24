package thatline.localup.auth.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import thatline.localup.auth.exception.EmailSendException

@Service
class EmailService(
    // 실행을 통해서 bean을 잡는거라 오류 표시나는 것.
    private val mailSender: JavaMailSender,
    private val emailVerificationRedisService: EmailVerificationRedisService,
) {
    companion object {
        private const val FROM_EMAIL = "noreply@localup.co.kr"
        private const val VERIFICATION_SUBJECT = "[LocalUp] 이메일 인증을 완료해주세요"
    }
    /**
     * 이메일 인증 링크를 전송합니다.
     *
     * @param email 수신자 이메일 주소
     * @param baseUrl 서버의 베이스 URL (인증 링크 생성용)
     */
    fun sendVerificationEmail(email: String, baseUrl: String) {
        val verificationToken = emailVerificationRedisService.generateVerificationToken()
        emailVerificationRedisService.saveVerificationToken(email, verificationToken)
        
        val verificationLink = "$baseUrl/api/auth/verify-email?email=$email&token=$verificationToken"

        val htmlContent = createVerificationEmailContent(verificationLink)

        sendEmail(
            to = email,
            subject = VERIFICATION_SUBJECT,
            htmlContent = htmlContent
        )
    }

    /**
     * 이메일 전송 공통 메서드
     *
     * @param to 수신자 이메일 주소
     * @param subject 이메일 제목
     * @param htmlContent HTML 형식의 이메일 내용
     */
    private fun sendEmail(to: String, subject: String, htmlContent: String) {
        try {
            val mimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, true, "UTF-8")

            helper.setFrom(FROM_EMAIL)
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(htmlContent, true) // HTML 형식으로 설정

            mailSender.send(mimeMessage)
        } catch (exception: Exception) {
            throw EmailSendException()
        }
    }


    /**
     * 이메일 인증 HTML 콘텐츠 생성
     *
     * @param verificationLink 인증 링크
     * @return HTML 형식의 이메일 내용
     */
    private fun createVerificationEmailContent(verificationLink: String): String {
        return """
              <!DOCTYPE html>
              <html lang="ko">
              <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>이메일 인증</title>
                  <style>
                      body { font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', sans-serif; line-height: 1.6; color: #333; }
                      .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                      .header { background-color: #4CAF50; color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                      .content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                      .button { display: inline-block; background-color: #4CAF50; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                      .footer { margin-top: 30px; text-align: center; color: #666; font-size: 12px; }
                  </style>
              </head>
              <body>
                  <div class="container">
                      <div class="header">
                          <h1>LocalUp 이메일 인증</h1>
                      </div>
                      <div class="content">
                          <h2>안녕하세요!</h2>
                          <p>LocalUp 서비스 가입을 환영합니다. 계정 활성화를 위해 이메일 인증을 완료해주세요.</p>
                          <p>아래 버튼을 클릭하시면 이메일 인증이 완료됩니다:</p>
                          <div style="text-align: center;">
                              <a href="$verificationLink" class="button">이메일 인증하기</a>
                          </div>
                          <p>버튼이 작동하지 않는 경우, 아래 링크를 브라우저에 직접 입력해주세요:</p>
                          <p style="word-break: break-all; color: #666;">$verificationLink</p>
                          <p><strong>참고:</strong> 이 링크는 24시간 동안만 유효합니다.</p>
                      </div>
                      <div class="footer">
                          <p>이 이메일은 LocalUp 시스템에서 자동으로 발송되었습니다.</p>
                          <p>문의사항이 있으시면 고객센터로 연락해주세요.</p>
                      </div>
                  </div>
              </body>
              </html>
          """.trimIndent()
    }
}