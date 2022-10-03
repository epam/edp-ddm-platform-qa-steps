package platform.qa.email;

import io.restassured.common.mapper.TypeRef;
import platform.qa.email.models.mailboxListResponse.MailUserModel;
import platform.qa.email.models.messageResponseModel.MailUserByIdModel;
import platform.qa.email.service.EmailService;
import platform.qa.entities.User;

import java.util.List;

public class EmailClientSteps {
    private final EmailService emailService = new EmailService();

    public List<MailUserModel> getUserMails(User user) {
        return emailService.getMailboxListByUser(user.getLogin())
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {});
    }

    public MailUserByIdModel getUserMailData(User user, String mailId) {
        return emailService.getMailInfoByUserNameAndMailId(user.getLogin(), mailId)
                .statusCode(200)
                .extract()
                .body()
                .as(MailUserByIdModel.class);
    }
}
