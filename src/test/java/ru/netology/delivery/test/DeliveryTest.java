package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    //раньше текст кнопки был "Забронировать"
    private static final String FORM_SEND_BUTTON_TEXT = "Запланировать";
    //раньше было "Встреча успешно забронирована на ..."
    private static final String SUCCESS_NOTIFICATION_CONTENT = "Встреча успешно запланирована на ";
    private static final String REPLAN_NOTIFICATION_TITLE = "Необходимо подтверждение";
    private static final String REPLAN_NOTIFICATION_BUTTON_TEXT = "Перепланировать";

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        // заполняем форму первый раз
        fillAndSendForm(validUser, firstMeetingDate);
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(exactText(SUCCESS_NOTIFICATION_CONTENT + firstMeetingDate), Duration.ofSeconds(15))
                .shouldBe(visible);

        // заполняем форму второй раз с другой датой
        open("http://localhost:9999");
        fillAndSendForm(validUser, secondMeetingDate);
        $("[data-test-id=replan-notification] .notification__title")
                .shouldHave(exactText(REPLAN_NOTIFICATION_TITLE), Duration.ofSeconds(15))
                .shouldBe(visible);
        $$("[data-test-id=replan-notification] .button").find(exactText(REPLAN_NOTIFICATION_BUTTON_TEXT)).click();
        //раньше было data-test-id=notification
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(exactText(SUCCESS_NOTIFICATION_CONTENT + secondMeetingDate), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    private void fillAndSendForm(DataGenerator.UserInfo userInfo, String date) {
        $("[data-test-id=city] input").setValue(userInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(date);
        $("[data-test-id=name] input[name=name]").setValue(userInfo.getName());
        $("[data-test-id=phone] input[name=phone]").setValue(userInfo.getPhone());
        $(".checkbox[data-test-id=agreement]").click();
        $$("form .button .button__text").find(exactText(FORM_SEND_BUTTON_TEXT)).click();
    }
}
