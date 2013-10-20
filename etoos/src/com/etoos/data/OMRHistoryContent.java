package com.etoos.data;


public class OMRHistoryContent 
{
	public Integer Number = 1 ; // 문제 번호
	public Integer 	QuestionType = 0 ;	// 문제 타입, 주관식인지 객관식인지. ( 객관식 0 , 주관식 1 )
	public Integer 	Answer = 0;	
	public String 	AnswerString = "";	// 주관식 정답 
	public Integer Timer	= 0;	// 타이머 ... 재선택시 원래 있던 시간에서 추가해서 처리해야한다. 
	
	
	public Integer	refQuestion = -1; // 문제 참조 번호 (문제 보기 버튼을 눌렀을 경우 )
	public Integer 	AnswerType = 0;
}
