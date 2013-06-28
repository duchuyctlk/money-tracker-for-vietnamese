package com.duchuyctlk.moneytracker.model;

public class DI__MoneyAmount {
	private int soLuong;
	private int loai; // 1 = choMuon, 0 = muon, 2 = thu, 3 = chi
	private String doiTuong;
        private int ngay; // 1->31
        private int thang; // 0->11
        private int nam;
	
	public DI__MoneyAmount() {
		soLuong = 0;
		loai = 1;
		doiTuong = "";
                ngay = 1;
                thang = 0;
                nam = 2000;
	}
	
	public DI__MoneyAmount(int sl, int l, String dt, int ng, int th, int n) {
		soLuong = sl;
		loai = l;
		doiTuong = dt;
                ngay = ng;
                thang = th;
                nam = n;
	}
	
	public void setSoLuong(int value) {
		soLuong = value;
	}
	
	public int getSoLuong() {
		return soLuong;
	}	
	
	public void setLoai(int value) {
		loai = value;
	}
	
	public int getLoai() {
		return loai;
	}
	
	public void setDoiTuong(String value) {
		doiTuong = value;
	}
	
	public String getDoiTuong() {
		return doiTuong;
	}

	public void setNgay(int value) {
		ngay = value;
	}

	public int getNgay() {
		return ngay;
	}

	public void setThang(int value) {
		thang = value;
	}

	public int getThang() {
		return thang;
	}

	public void setNam(int value) {
		nam = value;
	}

	public int getNam() {
		return nam;
	}
}
