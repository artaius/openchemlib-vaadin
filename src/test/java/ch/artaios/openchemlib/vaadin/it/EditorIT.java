package ch.artaios.openchemlib.vaadin.it;

import ch.artaios.openchemlib.vaadin.EditorTestView;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import in.virit.mopo.Mopo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EditorIT {

    @Value("${local.server.port}")
    private int port;

    static Playwright playwright = Playwright.create();

    private Browser browser;
    private Page page;
    private Mopo mopo;

    @BeforeEach
    public void setup() {
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setDevtools(true)
                );

        page = browser.newPage();
        page.setDefaultTimeout(5000); // die faster if needed
        mopo = new Mopo(page);
    }

    @AfterEach
    public  void closePlaywright() {
        page.close();
        browser.close();
    }

    @Test
    @Disabled
    public void smokeTestAllTestUIs() {
        String rootUrl = "http://localhost:" + port + "/";
        mopo.getViewsReportedByDevMode(browser, rootUrl).forEach(viewName -> {
            String url = rootUrl + viewName;
            page.navigate(url);
            mopo.assertNoJsErrors();
            System.out.println("Checked %s and it contained no JS errors.".formatted(viewName));
        });

    }

    @Test
    public void testEditorTestView() {
        String rootUrl = "http://localhost:" + port + "/" + EditorTestView.class.getSimpleName().toLowerCase();
        page.navigate(rootUrl);

        Locator locator = page.locator("#input-vaadin-text-field-20");
        locator.waitFor();
        assertThat(locator).not().isEmpty();

        locator = page.locator("openchemlib-editor").first();
        locator.waitFor();
        assertThat(locator).hasAttribute("idcode", Pattern.compile(".+"));

//        page.locator("vaadin-button").click();
//        assertThat(locator).hasValue("");
    }

}
