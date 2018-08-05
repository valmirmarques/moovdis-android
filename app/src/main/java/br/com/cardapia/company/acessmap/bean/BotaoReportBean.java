package br.com.cardapia.company.acessmap.bean;

import java.io.Serializable;

/**
 * Created by tvtios-01 on 02/11/17.
 */

public class BotaoReportBean implements Serializable {

    private String buttonName;
    private int imageButton;
    private String text;
    private String tag;

    public int getImageButtonBack() {
        return imageButtonBack;
    }

    public void setImageButtonBack(int imageButtonBack) {
        this.imageButtonBack = imageButtonBack;
    }

    private int imageButtonBack;

    public BotaoReportBean (String buttonName, int imageButton, String text, int imageButtonBack) {
        this.buttonName = buttonName;
        this.imageButton = imageButton;
        this.text = text;
        this.imageButtonBack = imageButtonBack;
    }

    /**
     *
     * @return
     */
    public String getButtonName() {
        return buttonName;
    }

    /**
     *
     * @param buttonName
     */
    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    /**
     *
     * @return
     */
    public int getImageButton() {
        return imageButton;
    }

    /**
     *
     * @param imageButton
     */
    public void setImageButton(int imageButton) {
        this.imageButton = imageButton;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BotaoReportBean that = (BotaoReportBean) o;

        return buttonName != null ? buttonName.equals(that.buttonName) : that.buttonName == null;

    }

    @Override
    public int hashCode() {
        return buttonName != null ? buttonName.hashCode() : 0;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "BotaoReportBean{" +
                "buttonName='" + buttonName + '\'' +
                ", imageButton=" + imageButton +
                ", text='" + text + '\'' +
                ", tag='" + tag + '\'' +
                ", imageButtonBack=" + imageButtonBack +
                '}';
    }
}
