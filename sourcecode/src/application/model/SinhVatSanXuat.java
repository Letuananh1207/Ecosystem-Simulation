package application.model;

import java.util.Random;

import javafx.application.Platform;

public class SinhVatSanXuat extends SinhVat {

    // Constructor không tham số, sẽ gọi constructor của lớp cha và gán hệ sinh thái
    public SinhVatSanXuat(HeSinhThai heSinhThai) {
        super(heSinhThai); // Gọi constructor của lớp cha để truyền hệ sinh thái
        this.energy = 100;
    }

    // Constructor nếu cần khởi tạo với tọa độ và hệ sinh thái
    public SinhVatSanXuat(int x, int y, HeSinhThai heSinhThai) {
        super(x, y, heSinhThai); // Gọi constructor của lớp cha để truyền tọa độ và hệ sinh thái
        this.energy = 100;
    }


    @Override
    public synchronized void sinhsan() {
        // Kiểm tra điều kiện chỉ sinh sản khi timeunit chia hết cho 10
        if (heSinhThai.buocThoiGianProperty().get() % 9 == 0) {
            Random random = new Random();

            // Lấy kích thước lưới
            int chieuNgang = heSinhThai.getChieuNgang();
            int chieuRong = heSinhThai.getChieuRong();

            // Lặp tối đa kích thước lưới để tìm ô trống ngẫu nhiên
            for (int i = 0; i < chieuNgang * chieuRong; i++) {
                // Chọn tọa độ ngẫu nhiên trên lưới
                int xMoi = random.nextInt(chieuNgang);
                int yMoi = random.nextInt(chieuRong);

                // Lấy ô tại tọa độ ngẫu nhiên
                O oMoi = heSinhThai.getO(xMoi, yMoi);

                // Kiểm tra nếu ô trống
                if (oMoi.getSinhvat() == null) {
                    // Tạo sinh vật mới tại ô trống
                    SinhVatSanXuat sinhVatMoi = new SinhVatSanXuat(xMoi, yMoi, heSinhThai);
                    oMoi.setSinhvat(sinhVatMoi);
                    System.out.println("Sinh vật sản xuất tại (" + this.x + ", " + this.y + ") đã sinh ra một sinh vật mới tại (" + xMoi + ", " + yMoi + ").");
                    // Tăng birthRate trong hệ sinh thái
                    Platform.runLater(() -> {
                        heSinhThai.birthRateProperty().set(heSinhThai.birthRateProperty().get() + 1);
                    });
                    return; // Chỉ sinh sản một lần trong mỗi chu kỳ
                }
            }

            // Nếu không tìm được ô trống
            System.out.println("Không tìm được ô trống để sinh sản.");
        }
    }


	@Override
    public synchronized void chet() {
        // Kiểm tra nếu năng lượng <= 0 thì sinh vật chết
        if (this.energy <= 0) {
            // Lấy ô hiện tại của sinh vật
            O oHienTai = heSinhThai.getO(this.x, this.y);
            if (oHienTai != null && oHienTai.getSinhvat() != null) {
                // Loại bỏ sinh vật khỏi ô
                oHienTai.setSinhvat(null);
            }
        }
    }
	
	public void quanghop() {
	    // Kiểm tra nếu năng lượng hiện tại đã đạt mức tối đa (200), không cần cộng thêm
	    if (this.energy >= 200) {
	        System.out.println("Năng lượng đã đủ, không cần quang hợp thêm.");
	        return;  // Nếu năng lượng >= 200, không thực hiện cộng năng lượng nữa
	    }
	    // Tạo một đối tượng Random
	    Random random = new Random();

	    // Lấy ngẫu nhiên giá trị từ 5 đến 10
	    int tangNangLuong = random.nextInt(6) + 5;  // nextInt(6) trả về giá trị trong phạm vi [0, 5], cộng thêm 5 để có giá trị trong phạm vi [5, 10]

	    // Tăng năng lượng theo giá trị ngẫu nhiên
	    this.energy += tangNangLuong;

	    System.out.println("Sinh vật quang hợp và tăng năng lượng thêm " + tangNangLuong + ". Năng lượng hiện tại: " + this.energy);
	}


}
