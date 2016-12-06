package com.fisher.hoverer.pop;

import android.os.Handler;
import android.view.WindowManager;

import com.google.gson.Gson;

public class DataPosition {
	private int top = 4;
	private double force_max = 0.06;
	private double force_min = 0.03;
	private double force = 0.03;

	private Handler handler;
	private WindowManager.LayoutParams wmParams;

	private int flag = 0;
	private double speedx;
	private double speedy;
	private long time;
	private double x;
	private double y;
	private long interval;

	public DataPosition( Handler handler, WindowManager.LayoutParams wmParams ) {
		this.handler = handler;
		this.wmParams = wmParams;
	}

	public void init( double x, double y ) {
		this.x = x;
		this.y = y;
		time = System.currentTimeMillis();
		force=force_min;
	}
	public void setXY( double x, double y ) {
		long time = System.currentTimeMillis();
		if ( flag == top ) {
			flag = 0;
			return;
		}
		speedx = ( x - this.x ) / ( time - this.time );
		speedy = ( y - this.y ) / ( time - this.time );

		this.x = x;
		this.y = y;
		this.time = time;
		flag++;
	}

	public void run() {
		interval = System.currentTimeMillis() - this.time;
		while ( true ) {
			x += speedx * ( interval );
			y += speedy * ( interval );
			wmParams.x = (int) ( x );
			wmParams.y = (int) y;
//				log( "position-> " + this );
			handler.sendEmptyMessage( 200 );
			if ( speedx > 0 )
				speedx -= force;
			else
				speedx += force;
			if ( speedy > 0 )
				speedy -= force;
			else
				speedy += force;
			if ( y > 1200 || y < 0 )
				speedy = -speedy;
			if ( x > 700 || x < 0 )
				speedx = -speedx;
			if ( Math.abs( speedx ) < 1 && Math.abs( speedy ) < 1 ) {
				force=force_max;
			}
			if ( Math.abs( speedx ) < 0.2 && Math.abs( speedy ) < 0.2 ) {
				break;
			}
			try {
				Thread.sleep( interval );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public String toString() {
		try {
			return new Gson().toJson( this );
		} catch ( Exception e ) {
			e.printStackTrace();
			return super.toString();
		}
	}


}
