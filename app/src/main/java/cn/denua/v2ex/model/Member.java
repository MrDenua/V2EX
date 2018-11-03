package cn.denua.v2ex.model;

/*
 * v2ex member model
 *
 * @author denua
 * @date 2018/10/21
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Member {

    protected int id;
    protected String username;
    protected String number;
    protected String avatar_normal;

    protected String status;

    protected String avatar_mini;
    protected String avatar_large;

    protected String website;
    protected String github;
    protected String psn;
    protected String twitter;

    protected String bio;
    protected String tagline;
    protected long created;
    protected String location;
    protected String btc;


    private String fixScheme(String url){
            return url.startsWith("https:")
                    ?url
                    :"https:" + url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAvatar_normal() {
        return avatar_normal==null
                ?getAvatar_mini()
                :fixScheme(avatar_normal).replace("mini","normal");
    }

    public void setAvatar_normal(String avatar_normal) {

        this.avatar_normal = avatar_normal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatar_mini() {
        return fixScheme(avatar_mini);
    }

    public void setAvatar_mini(String avatar_mini) {
        this.avatar_mini = fixScheme(avatar_mini);
    }

    public String getAvatar_large() {
        return avatar_large==null
                ?getAvatar_normal()
                :fixScheme(avatar_large).replace("mini","large");
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getPsn() {
        return psn;
    }

    public void setPsn(String psn) {
        this.psn = psn;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBtc() {
        return btc;
    }

    public void setBtc(String btc) {
        this.btc = btc;
    }
}
