package th.co.siamkubota.kubota.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import th.co.siamkubota.kubota.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTestCeny2 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTestCeny2() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.offlineBtn), withText("เริ่มงานใหม่แบบไม่มีอินเตอร์เน็ต"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.buttonWhite), withText("เริ่มงานใหม่"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction customSpinner = onView(
                allOf(withId(R.id.spinnerJobType),
                        withParent(allOf(withId(R.id.jobLayout),
                                withParent(withId(R.id.rootLayout))))));
        customSpinner.perform(scrollTo(), click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.rootLayout),
                        childAtPosition(
                                withId(R.id.listview),
                                2),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.rootLayout),
                        childAtPosition(
                                withId(R.id.listview),
                                1),
                        isDisplayed()));
        linearLayout2.perform(click());

        ViewInteraction linearLayout3 = onView(
                allOf(withId(R.id.rootLayout),
                        childAtPosition(
                                withId(R.id.listview),
                                4),
                        isDisplayed()));
        linearLayout3.perform(click());




                ViewInteraction appCompatEditText3 = onView(
                withId(R.id.editTextTaskCode));
        appCompatEditText3.perform(scrollTo(), replaceText("OJ33-3333-3333"));


        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.radioButton1), withText("เจ้าของรถ"),
                        withParent(allOf(withId(R.id.radioGroupUserType),
                                withParent(withId(R.id.rootLayout))))));
        appCompatRadioButton.perform(scrollTo(), click());

        ViewInteraction appCompatEditText4 = onView(
                withId(R.id.editTextName));
        appCompatEditText4.perform(scrollTo(), replaceText("pong"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                withId(R.id.editTextTel1));
        appCompatEditText5.perform(scrollTo(), replaceText("123"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                withId(R.id.editTextCarNumber));
        appCompatEditText6.perform(scrollTo(), replaceText("12"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                withId(R.id.editTextEngineNumber));
        appCompatEditText7.perform(scrollTo(), replaceText("13"), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
                withId(R.id.editTextWorkHours));
        appCompatEditText8.perform(scrollTo(), replaceText("20"), closeSoftKeyboard());

        ViewInteraction appCompatTextGPSLocation = onView(
                withId(R.id.editTextGPSLocation));
        appCompatTextGPSLocation.perform(scrollTo(), replaceText("200,-20"), closeSoftKeyboard());

       // pressBack();




//        ViewInteraction appCompatEditText12 = onView(
//                withId(R.id.editTextTel1));
//        appCompatEditText12.perform(scrollTo(), replaceText("123"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.nextButton), withText("ส่วนถัดไป"), isDisplayed()));
        appCompatButton3.perform(click());



        ViewInteraction selectableRoundedImageView = onView(
                allOf(withId(R.id.imageView),
                        withParent(withId(R.id.rootLayout)),
                        isDisplayed()));
        selectableRoundedImageView.perform(click());

        ViewInteraction selectableRoundedImageView2 = onView(
                allOf(withId(R.id.imageView),
                        withParent(withId(R.id.rootLayout)),
                        isDisplayed()));
        selectableRoundedImageView2.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.nextButton), withText("ส่วนถัดไป"), isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatEditText9 = onView(
                withId(R.id.editTextTotalCost));
        appCompatEditText9.perform(scrollTo(), replaceText("89"), closeSoftKeyboard());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.checkBoxUserAccept), withText("ข้าพเจ้าได้รับคำแนะนำเป็นที่เข้าใจอย่างดีแล้ว"),
                        withParent(withId(R.id.rootLayout))));
        appCompatCheckBox.perform(scrollTo(), click());

        ViewInteraction selectableRoundedImageView3 = onView(
                withId(R.id.imageCustomerSignature));
        selectableRoundedImageView3.perform(scrollTo(), click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.saveButton), withText("บันทึกลายมือชื่อ"),
                        withParent(withId(R.id.navigationControleLayout)),
                        isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction appCompatCheckBox2 = onView(
                allOf(withId(R.id.checkBoxTecnicianAccept), withText("ข้าพเจ้าได้ให้คำแนะนำแก่ลูกค้าให้เป็นที่เข้าใจอย่างดีแล้ว"),
                        withParent(withId(R.id.rootLayout))));
        appCompatCheckBox2.perform(scrollTo(), click());

        ViewInteraction selectableRoundedImageView4 = onView(
                withId(R.id.imageTechnicianSignature));
        selectableRoundedImageView4.perform(scrollTo(), click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.saveButton), withText("บันทึกลายมือชื่อ"),
                        withParent(withId(R.id.navigationControleLayout)),
                        isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.editTextTechnicianName), isDisplayed()));
        appCompatEditText10.perform(replaceText("po"), closeSoftKeyboard());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.nextButton), withText("ส่วนถัดไป"), isDisplayed()));
        appCompatButton9.perform(click());

        ViewInteraction centerRadioButton = onView(
                allOf(withId(R.id.noButton),
                        withParent(withId(R.id.choicesGroup)),
                        isDisplayed()));
        centerRadioButton.perform(click());

        ViewInteraction centerRadioButton2 = onView(
                allOf(withId(R.id.noButton),
                        withParent(withId(R.id.choicesGroup)),
                        isDisplayed()));
        centerRadioButton2.perform(click());

        ViewInteraction centerRadioButton3 = onView(
                allOf(withId(R.id.noButton),
                        withParent(withId(R.id.choicesGroup)),
                        isDisplayed()));
        centerRadioButton3.perform(click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(R.id.nextButton), withText("ส่วนถัดไป"), isDisplayed()));
        appCompatButton10.perform(click());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.confirmButton), withText("ยืนยัน"), isDisplayed()));
        appCompatButton11.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
