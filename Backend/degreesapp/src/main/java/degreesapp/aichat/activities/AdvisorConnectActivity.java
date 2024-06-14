package degreesapp.aichat.activities;

import degreesapp.aichat.base.AIChatActivity;
import degreesapp.controllers.chat.ChatSocket;

import java.util.Collection;

public class AdvisorConnectActivity extends AIChatActivity {
    @Override
    public void onStartup() {
        Collection<String> rooms = ChatSocket.getRooms();
        Response response;
        if ( rooms.isEmpty() ) {
            response = new Response("Okay. I'll take you to the advisor chat.");
            response.setClientAction("toAdvisorChat");
            sendResponse(response);
            this.removeSelf();
        } else {
            response = new Response();
            response.setText("I can take you to the advisor chat. First, which chatroom do you want to go to?");
            for ( String roomName : rooms ) {
                response.addRecommendedPrompt(new Prompt(roomName));
            }
            this.sendResponse(response);
            this.replaceWithNewActivity(
                    new AIChatActivity() {
                        @Override
                        public void onStartup() {
                        }

                        @Override
                        public void onCancel() {
                            sendResponse(new Response("Alright. Sorry about that."));
                        }

                        @Override
                        public void onPrompt(Prompt prompt) {
                            String chatRoomName = prompt.getText();
                            if ( rooms.contains(chatRoomName) ) {
                                Response response = new Response();
                                response.setClientAction("toAdvisorChat" , chatRoomName);
                                this.sendResponse(response);
                                this.removeSelf();
                            } else {
                                Response response = new Response();
                                this.replaceWithNewActivity(new AIChatActivity() {
                                    @Override
                                    public void onStartup() {
                                        Response response = new Response();
                                        response.setText("Are you sure you want to create a new chatroom?");
                                        response.addRecommendedPrompt(new Prompt("Yes"));
                                        response.addRecommendedPrompt(new Prompt("No"));
                                        sendResponse(response);
                                    }

                                    @Override
                                    public void onCancel() {
                                        sendResponse(new Response("Alright. Sorry about that."));
                                    }

                                    @Override
                                    public void onPrompt(Prompt prompt) {
                                        switch ( prompt.getText().toLowerCase() ) {
                                            case "yes":
                                            case "y": {
                                                Response response = new Response();
                                                response.setClientAction("toAdvisorChat" , chatRoomName);
                                                sendResponse(response);
                                                return;
                                            }
                                            case "no":
                                            case "n": {
                                                sendResponse(new Response("Alright. Sorry for the misunderstanding!"));
                                                removeSelf();
                                                return;
                                            }
                                            default: {
                                                getParentActivity().onPrompt(prompt);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void onCancel() {
        sendResponse(new Response("Alright. If you want to talk to your advisor, please let us know!"));
    }

    @Override
    public void onPrompt(Prompt prompt) {
        this.getParentActivity().onPrompt(prompt);
    }
}
