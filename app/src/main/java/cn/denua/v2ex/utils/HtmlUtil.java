package cn.denua.v2ex.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.model.Member;
import cn.denua.v2ex.model.Node;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.model.Tag;
import cn.denua.v2ex.model.Topic;

public class HtmlUtil {

    public static String[] getLoginFieldName(String html){

        String[] result = new String[4];
        Document document = Jsoup.parse(html);
        Elements elements = document.select(".sl");
        int i=0;
        for (Element element : elements) {
            String name = element.attr("name");
            result[i++] = name;
        }
        result[3] = document.selectFirst("input[name=once]").val();
        return result;
    }

    public static void attachRepliesAndDetail(Topic topic, String html){

        Document document = Jsoup.parse(html);
        Iterator<Element> it = document.select("#Main > .box > .cell > .fr > .tag").iterator();
        List<Tag> tags = new ArrayList<>();

        for ( ; it.hasNext(); ) {
            Element element = it.next();
            tags.add(new Tag(element.text()));
        }
        topic.setClicks(matcherGroup1Int(" · (\\d+) 次点击", html));
        topic.setTags(tags);

        String nodeName = matcherGroup1("<a href=\"/go/([^\"])+\">[^<]+</a>", html);
        String nodeTitle = matcherGroup1("<a href=\"/go/[^\"]+\">([^<]+)</a>", html);
        topic.setNode(new Node(nodeName, nodeTitle));
        topic.setFavors(matcherGroup1Int("∙  (\\d+) 人收藏 ", html));
        topic.setThanks(matcherGroup1Int("∙  (\\d+) 人感谢", html));
        topic.setCsrfToken(matcherGroup1("var csrfToken = \"([^\"]+)", html));
        topic.setReplies(matcherGroup1Int("<span class=\"gray\">(\\d+) 回复 &nbsp;<strong", html));

        attachReplies(topic, html);
    }

    public static void attachReplies(Topic topic, String html){

        if (topic.getReplies() == 0){
            topic.setReplyList(null);
            return;
        }

        Document document = Jsoup.parse(html);

        Iterator<Element> elementIterator = document.select("#Main > .box > .cell[id]").iterator();

        List<Reply> replies = new ArrayList<>();

        for (; elementIterator.hasNext(); ) {
            Element e = elementIterator.next();

            Reply reply = new Reply();
            String cell = e.toString();
            int id = matcherGroup1Int("id=\"r_(\\d+)\"", cell);

            reply.setId(id);
            reply.setMember(new Member(
                    matcherGroup1("href=\"/member/([^\"]+)\"", cell),
                    matcherGroup1("<img src=\"([^\"]+)\" class=\"avatar\"", cell)));
            reply.setAgo(matcherGroup1("<span class=\"ago\">([^\"]+前)", cell));
            reply.setVia(matcherGroup1("(via [^<]+)", cell));
            reply.setLike(matcherGroup1Int("<span class=\"small fade\">♥ (\\d+)</span>", cell));

            Element element = e.selectFirst(".reply_content");
            for (Element img:element.select("img")){
                img.attr("width","100%");
                img.attr("height","auto");
            }

            reply.setContent(element.html());

            replies.add(reply);
        }
        if (topic.getReplyList()!= null) {
            topic.getReplyList().addAll(replies);
        } else{
            topic.setReplyList(replies);
        }
    }

    public static void attachAccountInfo(Account account, String html){

        account.setFavorNodes(matcherGroup1Int("<span class=\"bigger\">(\\d+)</span>" +
                "<div class=\"sep3\"></div><span class=\"fade\">节点收藏</span>", html));
        account.setFavorTopics(matcherGroup1Int("<span class=\"bigger\">(\\d+)</span>" +
                "<div class=\"sep3\"></div><span class=\"fade\">主题收藏</span>", html));
        account.setFollowing(matcherGroup1Int("<span class=\"bigger\">(\\d+)</span>" +
                "<div class=\"sep3\"></div><span class=\"fade\">特别关注</span>", html));

        account.setNotifications(matcherGroup1Int("([\\d]+) 条未读提醒", html));
        account.setJoin(matcherGroup1("已在 ([\\d :+-]+) 完成验证", html));
        account.setNumber(matcherGroup1("V2EX 第 (\\d+) 号会员", html));

        account.setGold(matcherGroup1Int("(\\d+) <img src=\"/static/img/gold@2x.png\"" +
                " height=\"16\" ", html));
        account.setSilver(matcherGroup1Int("(\\d+) <img src=\"/static/img/silver@2x." +
                "png\" height=\"16\"" ,  html));
        account.setBronze(matcherGroup1Int("(\\d+) <img src=\"/static/img/bronze@2x" +
                ".png\" height=\"16\"", html));

        account.setBalance(account.getBronze()+account.getSilver()*100+account.getGold()*10000);
    }

    public static String applyHtmlStyle(String html){

        Document document = Jsoup.parse(html);
        for (Element img:document.select("img")){
            img.attr("width","100%");
            img.attr("height","auto");
        }

        return document.toString();
    }

    private static String matcherGroup1(String regex, String html){

        Matcher matcher = Pattern.compile(regex).matcher(html);
        if (matcher.find()){
            return matcher.group(1);
        }
        return "";
    }

    private static int matcherGroup1Int(String regex, String html){
        String res = matcherGroup1(regex, html);
        return res.equals("") ? 0: Integer.valueOf(res);
    }
}
