package com.astakos.not.activities;

public class Message {
    
    String mesajText;
    String gonderici;
	String eposta;
	String avatar;
	String resim;
    String zaman;
	String uid;

    public Message() {
    }

    public Message(String mesajText, String gonderici, String eposta, String avatar, String resim, String zaman,String uid) {
        this.mesajText = mesajText;
        this.gonderici = gonderici;
		this.eposta = eposta;
		this.avatar = avatar;
		this.resim = resim;
        this.zaman = zaman;
		this. uid = uid;
    }

    public String getMesajText() {
        return mesajText;
    }

    public void setMesajText(String mesajText) {
        this.mesajText = mesajText;
    }

    public String getGonderici() {
        return gonderici;
    }
	
	public void setGonderici(String gonderici){
		this.gonderici = gonderici;
	}
	
	public String getEposta() {
        return eposta;
    }

	public void setEposta(String eposta){
		this.eposta = eposta;
	}
	
	public String getAvatar(){
		return avatar;
	}

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
	
	public String getResim(){
		return resim;
	}

    public void setResim(String resim) {
        this.resim = resim;
    }
	

    public String getZaman() {
        return zaman;
    }

    public void setZaman(String zaman) {
        this.zaman = zaman;
    }
	public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
