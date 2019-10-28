package rocketchat.spring.rest.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

/**
 * postMessage request https://rocket.chat/docs/developer-guides/rest-api/chat/postmessage/
 */
@SuppressWarnings("unused")
public class PostMessage {

  /**
   * The room id of where the message is to be sent.
   */
  private String roomId;

  /**
   * The channel name with the prefix in front of it.
   */
  private String channel;

  /**
   * The text of the message to send, is optional because of attachments.
   */
  private String text;

  /**
   * This will cause the message’s name to appear as the given alias, but your username will still display.
   */
  private String alias;

  /**
   * If provided, this will make the avatar on this message be an emoji. http://emoji.codes/
   */
  private String emoji;

  /**
   * If provided, this will make the avatar use the provided image url
   */
  private String avatar;

  /**
   * Optional attachments
   */
  private Attachment[] attachments;

  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getEmoji() {
    return emoji;
  }

  public void setEmoji(String emoji) {
    this.emoji = emoji;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public Attachment[] getAttachments() {
    return attachments;
  }

  public void setAttachments(Attachment[] attachments) {
    this.attachments = attachments;
  }

  public PostMessage roomId(String roomId) {
    setRoomId(roomId);
    return this;
  }

  public PostMessage channel(String channel) {
    setChannel(channel);
    return this;
  }

  public PostMessage text(String text) {
    setText(text);
    return this;
  }

  public PostMessage alias(String alias) {
    setAlias(alias);
    return this;
  }

  public PostMessage emoji(String emoji) {
    setEmoji(emoji);
    return this;
  }

  public PostMessage avatar(String avatar) {
    setAvatar(avatar);
    return this;
  }

  public PostMessage attachments(Attachment... attachments) {
    setAttachments(attachments);
    return this;
  }

  @Override
  public String toString() {
    return "PostMessage{" +
        "roomId='" + roomId + '\'' +
        ", channel='" + channel + '\'' +
        ", text='" + text + '\'' +
        ", alias='" + alias + '\'' +
        ", emoji='" + emoji + '\'' +
        ", avatar='" + avatar + '\'' +
        ", attachments=" + Arrays.toString(attachments) +
        '}';
  }

  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  public static class Attachment {

    /**
     * The color you want the order on the left side to be, any value background-css supports.
     */
    private String color;

    /**
     * The text to display for this attachment, it is different than the message’s text.
     */
    private String text;

    /**
     * Displays the time next to the text portion.
     */
    private Instant ts;

    /**
     * An image that displays to the left of the text, looks better when this is relatively small.
     */
    private String thumbUrl;

    /**
     * Only applicable if the ts is provided, as it makes the time clickable to this link.
     */
    private String messageLink;

    /**
     * Causes the image, audio, and video sections to be hiding when collapsed is true.
     */
    private boolean collapsed;

    /**
     * Name of the author.
     */
    private String authorName;

    /**
     * Providing this makes the author name clickable and points to this link.
     */
    private String authorLink;

    /**
     * Displays a tiny icon to the left of the Author’s name.
     */
    private String authorIcon;

    /**
     * Title to display for this attachment, displays under the author.
     */
    private String title;

    /**
     * Providing this makes the title clickable, pointing to this link.
     */
    private String titleLink;

    /**
     * When this is true, a download icon appears and clicking this saves the link to file.
     */
    private boolean titleLinkDownload;

    /**
     * The image to display, will be “big” and easy to see.
     */
    private String imageUrl;

    /**
     * Audio file to play, only supports what html audio does.
     */
    private String audioUrl;

    /**
     * Video file to play, only supports what html video does.
     */
    private String videoUrl;

    /**
     * An array of Attachment Field Objects.
     */
    private Field[] fields;

    public String getColor() {
      return color;
    }

    public void setColor(String color) {
      this.color = color;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    public Instant getTs() {
      return ts;
    }

    public void setTs(Instant ts) {
      this.ts = ts;
    }

    public String getThumbUrl() {
      return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
      this.thumbUrl = thumbUrl;
    }

    public String getMessageLink() {
      return messageLink;
    }

    public void setMessageLink(String messageLink) {
      this.messageLink = messageLink;
    }

    public boolean isCollapsed() {
      return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
      this.collapsed = collapsed;
    }

    public String getAuthorName() {
      return authorName;
    }

    public void setAuthorName(String authorName) {
      this.authorName = authorName;
    }

    public String getAuthorLink() {
      return authorLink;
    }

    public void setAuthorLink(String authorLink) {
      this.authorLink = authorLink;
    }

    public String getAuthorIcon() {
      return authorIcon;
    }

    public void setAuthorIcon(String authorIcon) {
      this.authorIcon = authorIcon;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getTitleLink() {
      return titleLink;
    }

    public void setTitleLink(String titleLink) {
      this.titleLink = titleLink;
    }

    public boolean isTitleLinkDownload() {
      return titleLinkDownload;
    }

    public void setTitleLinkDownload(boolean titleLinkDownload) {
      this.titleLinkDownload = titleLinkDownload;
    }

    public String getImageUrl() {
      return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
      return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
      this.audioUrl = audioUrl;
    }

    public String getVideoUrl() {
      return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
      this.videoUrl = videoUrl;
    }

    public Field[] getFields() {
      return fields;
    }

    public void setFields(Field[] fields) {
      this.fields = fields;
    }

    public Attachment color(String color) {
      setColor(color);
      return this;
    }

    public Attachment text(String text) {
      setText(text);
      return this;
    }

    public Attachment thumbUrl(String thumbUrl) {
      setThumbUrl(thumbUrl);
      return this;
    }

    public Attachment messageLink(String messageLink) {
      setMessageLink(messageLink);
      return this;
    }

    public Attachment collapsed(boolean collapsed) {
      setCollapsed(collapsed);
      return this;
    }

    public Attachment authorName(String authorName) {
      setAuthorName(authorName);
      return this;
    }

    public Attachment authorLink(String authorLink) {
      setAuthorLink(authorLink);
      return this;
    }

    public Attachment authorIcon(String authorIcon) {
      setAuthorIcon(authorIcon);
      return this;
    }

    public Attachment title(String title) {
      setTitle(title);
      return this;
    }

    public Attachment titleLink(String titleLink) {
      setTitleLink(titleLink);
      return this;
    }

    public Attachment titleLinkDownload(boolean titleLinkDownload) {
      setTitleLinkDownload(titleLinkDownload);
      return this;
    }

    public Attachment imageUrl(String imageUrl) {
      setImageUrl(imageUrl);
      return this;
    }

    public Attachment audioUrl(String audioUrl) {
      setAudioUrl(audioUrl);
      return this;
    }

    public Attachment videoUrl(String videoUrl) {
      setVideoUrl(videoUrl);
      return this;
    }

    public Attachment fields(Field... fields) {
      setFields(fields);
      return this;
    }

    @Override
    public String toString() {
      return "Attachment{" +
          "color='" + color + '\'' +
          ", text='" + text + '\'' +
          ", ts=" + ts +
          ", thumbUrl='" + thumbUrl + '\'' +
          ", messageLink='" + messageLink + '\'' +
          ", collapsed=" + collapsed +
          ", authorName='" + authorName + '\'' +
          ", authorLink='" + authorLink + '\'' +
          ", authorIcon='" + authorIcon + '\'' +
          ", title='" + title + '\'' +
          ", titleLink='" + titleLink + '\'' +
          ", titleLinkDownload=" + titleLinkDownload +
          ", imageUrl='" + imageUrl + '\'' +
          ", audioUrl='" + audioUrl + '\'' +
          ", videoUrl='" + videoUrl + '\'' +
          ", fields=" + Arrays.toString(fields) +
          '}';
    }

    public static class Field {

      /**
       * Whether this field should be a short field.
       */
      private boolean isShort;

      /**
       * The title of this field.
       */
      private String title;

      /**
       * The value of this field, displayed underneath the title value.
       */
      private String value;

      @JsonProperty("short")
      public boolean isShort() {
        return isShort;
      }

      public void setShort(boolean aShort) {
        isShort = aShort;
      }

      public String getTitle() {
        return title;
      }

      public void setTitle(String title) {
        this.title = title;
      }

      public String getValue() {
        return value;
      }

      public void setValue(String value) {
        this.value = value;
      }

      public Field isShort(boolean isShort) {
        setShort(isShort);
        return this;
      }

      public Field title(String title) {
        setTitle(title);
        return this;
      }

      public Field value(String value) {
        setValue(value);
        return this;
      }

      @Override
      public String toString() {
        return "Field{" +
            "isShort=" + isShort +
            ", title='" + title + '\'' +
            ", value='" + value + '\'' +
            '}';
      }
    }
  }

  public static class Reply {
    private Date ts;
    private String channel;
    private boolean success;

    public Date getTs() {
      return ts;
    }

    public void setTs(Date ts) {
      this.ts = ts;
    }

    public String getChannel() {
      return channel;
    }

    public void setChannel(String channel) {
      this.channel = channel;
    }

    public boolean isSuccess() {
      return success;
    }

    public void setSuccess(boolean success) {
      this.success = success;
    }

    @Override
    public String toString() {
      return "Reply{" +
          "ts=" + ts +
          ", channel='" + channel + '\'' +
          ", success=" + success +
          '}';
    }
  }
}
