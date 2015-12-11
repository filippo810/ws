package domain.wrapper;



public class VADomain {

	private String domanda;
	private String risposta;


	
	//private String suggestion;
	private String suggestion_link;
	private Object[] suggestion;
	

	public Object[] getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(Object[] sugg) {
		this.suggestion = sugg;
	}

	private String title;
	private String question;

	public String getSuggestion_link() {
		return suggestion_link;
	}

	public void setSuggestion_link(String suggestion_link) {
		this.suggestion_link = suggestion_link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}

	public String getRisposta() {
		return risposta;
	}

	public String getDomanda() {
		return domanda;
	}

	public void setDomanda(String domanda) {
		this.domanda = domanda;
	}



}
