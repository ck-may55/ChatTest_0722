package com.example.chie.notifitest0429;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String userId;
    private OnFragmentInteractionListener mListener;

    private ListView chatView;
    private Button sendButton;
    private EditText messageText;

    private FirebaseDatabase database;

    //DBに登録する内容
    private String message;
    private String mPhotoUrl = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg";
    private String mToUserId = "susmedroot";

    public ChatPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatPage.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatPage newInstance(String param1, String param2) {
        ChatPage fragment = new ChatPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.userId = getArguments().getString("USER_ID").toUpperCase();
        Log.d("ChatPage", "onCreate " + this.userId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_chat_page, container, false);

        Log.d("ChatPage", "onCreateView 1");

        // ListViewを使ったチャットページの表示
        View view = inflater.inflate(R.layout.fragment_chat_page, container, false);
        chatView = (ListView)view.findViewById(R.id.list_chat);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_chat);
        adapter.add("sample");
        chatView.setAdapter(adapter);

        sendButton = (Button) view.findViewById(R.id.send_button);
        messageText = (EditText) view.findViewById(R.id.pills_name_text);
        // 初期化
        initChatView();

        //送信ボタンでチャットDBとListViewに追加
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = messageText.getText().toString();
                sendToDB(userId,message);
                adapter.add(message);
                messageText.getEditableText().clear();
            }
        });

        //WebViewを使ったチャットページの表示
        /*
        WebView chatWebView = (WebView)view.findViewById(R.id.chat_view);
        chatWebView.getSettings().setUseWideViewPort(true);
        chatWebView.getSettings().setLoadWithOverviewMode(true);

        Log.d("ChatPage", "onCreateView 2");

        String url = getString(R.string.chatpage_url);
        // パラメータemaillocalにuserIdを小文字にしてセットする
        url += "?emaillocal=" + userId.toLowerCase();

        // SharedPreferencesからセーブしてあったパスワードを得る
        Context context = getActivity().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("FCMessages", Context.MODE_PRIVATE);
        String pwd = sp.getString("PWD", "");

        // 00を外してパスワードをセット
        url += "&password=" + pwd.substring(0, 4);

        Log.d("ChatPage", "url: " + url);

        // 作成したurlでWeb Viewを立ち上げる
        chatWebView.loadUrl(url);

        Log.d("ChatPage", "onCreateView 3");

        //jacascriptを許可する
        chatWebView.getSettings().setJavaScriptEnabled(true);

        Log.d("ChatPage", "onCreateView 4");
        */

        // 戻るボタンのセット
        setBackButton();
        return view;
    }



    /**
     * ListView内の初期化。
     */
    private void initChatView(){
        database = FirebaseDatabase.getInstance();
        final DatabaseReference refChat = database.getReference("messages");
        //ユーザと担当者とのやり取りをListViewに表示

    }

    /**
     * FirebaseDBのmessage下にメッセージの内容を登録
     */
    private void sendToDB(String userId, String message){
        database = FirebaseDatabase.getInstance();
        //messagesでテスト中
        final DatabaseReference refChat = database.getReference("messages1");
        final ChatData chatData = new ChatData();
        chatData.photoUrl = mPhotoUrl;
        chatData.text = message;
        chatData.toUserid = mToUserId;
        chatData.userId = userId.toLowerCase();
        refChat.push().setValue(chatData);
    }

    /**
     * ListViewに送信メッセージを表示
     */
    private void addToList(String message){

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onBack();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setBackButton() {
        android.widget.Button left_button = (Button) getActivity().findViewById(R.id.left_back_button);

        left_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("ChatPage", "onClick back button");

                if (mListener != null) {
                    //
                    mListener.onBack();
                }
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onBack();
        void onFragmentInteraction(Uri uri);
    }
}
