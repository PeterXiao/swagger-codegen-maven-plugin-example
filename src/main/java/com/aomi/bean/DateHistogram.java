/**
 *
 */
package com.aomi.bean;

/**
 * @author LiY
 * @time 下午6:12:30
 */
public class DateHistogram {
	private String field;
	private String interval;
	private ExtendedBounds extended_bounds;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public ExtendedBounds getExtended_bounds() {
		return extended_bounds;
	}

	public void setExtended_bounds(ExtendedBounds extended_bounds) {
		this.extended_bounds = extended_bounds;
	}

}
