package degreesapp.aichat.activities;

import degreesapp.aichat.base.AIChatActivity;

public class MainChatActivity extends AIChatActivity {
    @Override
    public void onStartup() {
        Response response = new Response("Hello, " + getUser().getUserName() + "! How may I help you today?");
        response.addRecommendedPrompt(new Prompt("I want to talk to my advisor"));
        response.addRecommendedPrompt(new Prompt("Register me for a course"));
        response.addRecommendedPrompt(new Prompt("I want to make an academic plan"));
        sendResponse(response);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onPrompt(Prompt prompt) {
        String promptText = prompt.getText();
        if (promptText.toLowerCase().equals("never mind")) {
            if (getChildActivity() == null) {
                this.sendResponse(new Response("Okay."));
                return;
            } else {
                this.getCurrentActivity().onCancel();
                this.getCurrentActivity().removeSelf();
            }
            return;
        }
        switch (promptText.toLowerCase()) {
            case "i want to talk to my advisor":
                this.addChildActivity(new AdvisorConnectActivity());
                return;
            case "register me for a course":
                this.addChildActivity(new CourseRegisterActivity());
                return;
            case "i want to make an academic plan": {
                Response response = new Response();
                response.setClientAction("goToAcademicPlan");
                this.sendResponse(response);
                return;
            }
            default:
                this.sendResponse(new Response("Sorry, I didn't understand that. Could you please try again?"));
        }
    }
}