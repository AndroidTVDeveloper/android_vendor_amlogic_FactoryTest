package cn.com.factorytest;

import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Date;

public class VideoFragment extends Fragment implements MediaPlayer.OnCompletionListener{

	private static final String TAG = Tools.TAG;
	private String uri = "";
    TextView mTestTime;
    VideoView mVideoView;
    Context mContext;
    Date m_StartDate = new Date();
    Handler mVideoHandler = new VideoHandler();
    final int MSG_UPDATE_TIME =  0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mVideoView = (VideoView)view.findViewById(R.id.VideoView);
        mTestTime = (TextView)view.findViewById(R.id.TestTime);
        uri = "android.resource://" + mContext.getPackageName() + "/" + R.raw.testvideo;
        mVideoView.setVideoURI(Uri.parse(uri));
        mVideoView.start();
        mVideoView.setOnPreparedListener(new OnPreparedListener(){

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Log.i(TAG, "mVideoView player is onPrepared !!! ");
				mp.start();// 播放
				mp.setLooping(true);
			}

        });

        mVideoView.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
			Log.i(TAG, "mVideoView player is end !!! ");
		    mVideoView.setVideoURI(Uri.parse(uri));
	        mVideoView.start();
			}

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mVideoHandler.sendEmptyMessage(MSG_UPDATE_TIME);
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoHandler.removeMessages(MSG_UPDATE_TIME);
        mVideoView.pause();
        mVideoView.stopPlayback();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mVideoView.seekTo(0);
    }

    class VideoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_UPDATE_TIME:
                    mTestTime.setText(getTime());
                    mVideoHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, 1000);
                    break;
            }
        }
    };

    private String getTime() {
        Date newDate = new Date();

        long between = (newDate.getTime() - m_StartDate.getTime() ) / 1000;
        long day1 = between / ( 24 * 3600 );
        long hour1 = between % ( 24 * 3600 ) / 3600;
        long minute1 = between % 3600 / 60;
        long second1 = between % 60;


        if(between > (60 * 60 * 2) )
        {
            return getResources().getString(R.string.long_test_finish);
        }
        else
        {
            return "" + day1 + " : " + hour1 +  " : " + minute1 + " : " +  second1 ;
        }
    }
}
