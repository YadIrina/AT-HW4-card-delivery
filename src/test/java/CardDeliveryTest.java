import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;


public class CardDeliveryTest {
    private String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldTest() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id='city'] input").setValue("Петропавловск-Камчатский");
        String planningDate = generateDate(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79820000000");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    void shouldTestCityInList7Days() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id='city'] input").setValue("Пе");
        $$(".menu-item__control").findBy(Condition.text("Петропавловск-Камчатский")).click();
        String planningDate = generateDate(7, "dd.MM.yyyy");
        $("[data-test-id='date'] input").click();

        if (!generateDate(3, "MM").equals(generateDate(7, "MM"))) {
            $("body > div.popup.popup_direction_bottom-left.popup_target_anchor.popup_size_s.popup_visible.popup_padded.popup_theme_alfa-on-white > div > div > div > div > div > div > div:nth-child(4)").click();
        }
        $$("[data-day]").findBy(Condition.text(generateDate(7, "d"))).click();

        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79820000000");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));

    }
}
