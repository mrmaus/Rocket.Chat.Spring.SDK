package rocketchat.spring.ws;

/**
 * The conversation message model https://rocket.chat/docs/developer-guides/realtime-api/the-message-object/
 */
public class MessageObject {

  private String _id;
  private String rid;
  private String msg;
  private String tmid;
  private String t;
  private Integer tcount;

  public String getRid() {
    return rid;
  }

  public void setRid(String rid) {
    this.rid = rid;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getTmid() {
    return tmid;
  }

  public void setTmid(String tmid) {
    this.tmid = tmid;
  }

  public String getT() {
    return t;
  }

  public void setT(String t) {
    this.t = t;
  }

  public Integer getTcount() {
    return tcount;
  }

  public void setTcount(Integer tcount) {
    this.tcount = tcount;
  }
}
