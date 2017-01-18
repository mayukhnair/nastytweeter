import org.apache.http.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import twitter4j.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by Mayukh Nair on 18-Jan-17.
 */
public class NastyTweeter {

    Twitter twitter = TwitterFactory.getSingleton();
    Status grabbedTweet,builtTweet;
    QueryResult result;

    private void scanTweets() {

        while(true){
            Query query = new Query("@datnastytweeter");
            try {
                result = twitter.search(query);
                grabbedTweet = result.getTweets().get(0);
                sendReply(grabbedTweet);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }

    }

    private String fetchCurse(String screenName){
            String[] urlTypes = {"from","namefrom"};
            String[] namecurseUrls = {"back","blackadder","chainsaw","cocksplat","donut","ing","gfy","king","look","nugget","off","outside","problem","shakespeare","shutup","think","thinking","yoda","you"};
            String[] curseUrls = {"bag","because","bucket","bye","diabetes","family","fascinating","flying","horse","looking","no","maybe","pink","ridiculous","sake","shit","thanks","too","zayn","zero"};
            String pickedUrl,res=null;
            switch (new Random().nextInt(urlTypes.length)){
                case 0: pickedUrl = curseUrls[new Random().nextInt(curseUrls.length)];
                        res = fetchApiResponse("http://foaas.com/"+pickedUrl+"/Joe");
                        break;
                case 1: pickedUrl = namecurseUrls[new Random().nextInt(namecurseUrls.length)];
                        res = fetchApiResponse("http://foaas.com/"+pickedUrl+"/@"+screenName+"/Joe");
                        break;
            }
            return res;
    }

    private String fetchApiResponse(String s){
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(s);
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("HTTP Error. Code returned: " + response.getStatusLine().getStatusCode());
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            while (bufferedReader.readLine()!= null){
                return bufferedReader.readLine();
            }
            httpClient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void sendReply(Status grabbedTweet) throws TwitterException{
        String curse = fetchCurse(grabbedTweet.getUser().getScreenName());
        if(!curse.equals(null)){
            StatusUpdate statusUpdate = new StatusUpdate(curse);
            statusUpdate.inReplyToStatusId(grabbedTweet.getId());
            builtTweet = twitter.updateStatus(statusUpdate);
        }
    }

    public static void main (String[] args) {
        System.out.println("Hi");
        System.out.println("Now monitoring tweets");
        NastyTweeter nastyTweeter = new NastyTweeter();
        nastyTweeter.scanTweets();
    }



}
