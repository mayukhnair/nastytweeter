import org.apache.http.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import twitter4j.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

/**
 * Created by Mayukh Nair on 18-Jan-17.
 */
public class NastyTweeter {

    Twitter twitter = TwitterFactory.getSingleton();
    Status builtTweet;
    QueryResult result;
    Long prevId = 0l;
    Long currentId = 0l;
    Long[] repliedIds = new Long[1];

    int searchCount;
    long lastTweetId = Long.MAX_VALUE;

    private void scanTweets() {
            Thread tweetScannerThread = new Thread(new Runnable() {
                public void run() {
                   while (true){
                       do {
                           try {
                               Query query = new Query("@datnastytweeter +exclude:retweets");
                               query.setCount(100);
                               result = twitter.search(query);
                               searchCount = result.getTweets().size();
                               for (Status grabbedTweet : result.getTweets()){
                                   currentId = grabbedTweet.getId();
//                                   System.out.println(currentId);
                                       System.out.println(grabbedTweet.getId()+":"+grabbedTweet.getText());
//                                       sendReply(grabbedTweet);
                               }
                           } catch (TwitterException e) {
                               e.printStackTrace();
                           }
                       }
                       while (searchCount != 0 && searchCount % 100 == 0);
                   }


//                    while(true){
//                        try {
//                            Query query = new Query("@datnastytweeter");
//                            result = twitter.search(query);
//                            grabbedTweet = result.getTweets().get(0);
//                            Long currentId = result.getTweets().get(0).getId();
//                            if(!currentId.equals(prevId)){
//                            sendReply(grabbedTweet);
//                            prevId = currentId;
//                            }
//                        } catch (TwitterException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
            });
            tweetScannerThread.start();
    }

    private String fetchCurse(String screenName){
            String[] urlTypes = {"from","namefrom"};
            String[] namecurseUrls = {"back","blackadder","chainsaw","cocksplat","donut","ing","gfy","king","look","nugget","off","outside","problem","shakespeare","shutup","think","thinking","yoda","you"};
            String[] curseUrls = {"bag","because","bucket","bye","diabetes","family","fascinating","flying","horse","looking","no","maybe","pink","ridiculous","sake","shit","thanks","too","zayn","zero"};
            String pickedUrl,res=null;
            switch (new Random().nextInt(urlTypes.length)){
                case 0: pickedUrl = curseUrls[new Random().nextInt(curseUrls.length)];
                        res = "@"+screenName+" "+fetchApiResponse("http://foaas.com/"+pickedUrl+"/Joe");
                        break;
                case 1: pickedUrl = namecurseUrls[new Random().nextInt(namecurseUrls.length)];
                        res = fetchApiResponse("http://foaas.com/"+pickedUrl+"/@"+screenName+"/Joe");
                        break;
            }
            System.out.println(res);
            return res;
    }

    private String fetchApiResponse(String s){
        String curse = null;
        try {
            HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()).build();
            URI apiUri = new URI(s);
            System.out.println("Get url: "+s);
            HttpGet httpGet = new HttpGet(apiUri);
            httpGet.addHeader("accept", "text/plain");
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("HTTP Error. Code returned: " + response.getStatusLine().getStatusCode());
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
//            System.out.println("BufferedReader response: "+bufferedReader.readLine());
            curse = bufferedReader.readLine();
            httpClient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return curse;
    }

    private void sendReply(Status grabbedTweet) throws TwitterException{
        String curse = fetchCurse(grabbedTweet.getUser().getScreenName());
        System.out.println("Curse: "+curse);
        if(!curse.equals(null)){
            StatusUpdate statusUpdate = new StatusUpdate(curse);
            statusUpdate.inReplyToStatusId(grabbedTweet.getId());
            builtTweet = twitter.updateStatus(statusUpdate);
            System.out.println("Reply sent");
        }
    }

    public static void main (String[] args) {
        System.out.println("Hi");
        System.out.println("Now monitoring tweets");
        NastyTweeter nastyTweeter = new NastyTweeter();
        nastyTweeter.scanTweets();
    }



}
