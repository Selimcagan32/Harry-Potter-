import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class proje2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String dosyaAdi = "karakterler.txt";

        System.out.print("Giriş için şifre girin: ");
        String sifre = scanner.nextLine();
        if (!sifre.equals("1234")) {
            System.out.println("Hatalı şifre. Program sonlandırılıyor...");
            return;
        }

        while (true) {
            System.out.println("--- Ana Menü ---");
            System.out.println("1. API'den veri çek ve dosyaya yaz");
            System.out.println("2. Listele");
            System.out.println("3. Güncelle");
            System.out.println("4. Sil");
            System.out.println("5. Favoriye karakter ekle");
            System.out.println("6. Dosya yedeği oluştur");
            System.out.println("7. Çıkış");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            if (secim.equals("1")) {
                veriCek(dosyaAdi);
            } else if (secim.equals("2")) {
                listelemeMenusu(dosyaAdi, scanner);
            } else if (secim.equals("3")) {
                guncelle(dosyaAdi, scanner);
            } else if (secim.equals("4")) {
                sil(dosyaAdi, scanner);
            } else if (secim.equals("5")) {
                favoriyeEkle(dosyaAdi, scanner);
            } else if (secim.equals("6")) {
                dosyaYedekle(dosyaAdi);
            } else if (secim.equals("7")) {
                System.out.println("Programdan çıkılıyor...");
                break;
            } else {
                System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }

            System.out.println("\nDevam etmek için Enter'a bas...");
            scanner.nextLine();
        }

        scanner.close();
    }

    public static void veriCek(String dosyaAdi) {
        String apiUrl = "https://hp-api.onrender.com/api/characters";
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection baglanti = (HttpURLConnection) url.openConnection();
            baglanti.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(baglanti.getInputStream()));
            StringBuilder jsonMetni = new StringBuilder();
            String satir;
            while ((satir = reader.readLine()) != null) {
                jsonMetni.append(satir);
            }
            reader.close();

            String veri = jsonMetni.toString();
            String[] karakterler = veri.split("\\},\\{");

            BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dosyaAdi), "UTF-8"));
            fw.write("Ad;Ev;Patronus;Doğum;Durum\n");

            int sayac = 0;
            for (String karakter : karakterler) {
                if (sayac >= 30) break;

                String ad = araVeAl(karakter, "\"name\":\"", "\"");
                String ev = araVeAl(karakter, "\"house\":\"", "\"");
                String patronus = araVeAl(karakter, "\"patronus\":\"", "\"");
                String dogum = araVeAl(karakter, "\"dateOfBirth\":\"", "\"");
                String ogrenciMi = karakter.contains("\"hogwartsStudent\":true") ? "Öğrenci" : "Değil";

                String yazilacak = ad + ";" + ev + ";" + patronus + ";" + dogum + ";" + ogrenciMi + "\n";
                fw.write(yazilacak);
                sayac++;
            }

            fw.close();
            System.out.println("Veriler başarıyla karakterler.txt dosyasına yazıldı.");
        } catch (Exception e) {
            System.out.println("Hata oluştu: " + e.getMessage());
            logYaz("API bağlantı hatası: " + e.getMessage());
        }
    }

    public static void logYaz(String mesaj) {
        try {
            BufferedWriter logYazici = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("log.txt", true), "UTF-8"));
            String zaman = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
            logYazici.write("[" + zaman + "] " + mesaj + "\n");
            logYazici.close();
        } catch (Exception e) {
            System.out.println("log.txt dosyasına yazılamadı.");
        }
    }
        public static void listelemeMenusu(String dosyaAdi, Scanner scanner) {
        while (true) {
            System.out.println("--- Listeleme Menüsü ---");
            System.out.println("1. Tüm karakterleri listele");
            System.out.println("2. Ev bilgisine göre filtrele");
            System.out.println("3. Patronus bilgisine göre filtrele");
            System.out.println("4. Sadece öğrencileri listele");
            System.out.println("5. Favori karakterleri listele");
            System.out.println("6. Ana menüye dön");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            if (secim.equals("1")) {
                tumunuListele(dosyaAdi);
            } else if (secim.equals("2")) {
                System.out.print("Ev adı girin (örneğin: Gryffindor): ");
                String ev = scanner.nextLine();
                filtrele(dosyaAdi, ev, 1);
            } else if (secim.equals("3")) {
                System.out.print("Patronus adı girin (örneğin: Stag): ");
                String patronus = scanner.nextLine();
                filtrele(dosyaAdi, patronus, 2);
            } else if (secim.equals("4")) {
                filtrele(dosyaAdi, "Öğrenci", 4);
            } else if (secim.equals("5")) {
                favorileriListele();
            } else if (secim.equals("6")) {
                break;
            } else {
                System.out.println("Geçersiz seçim.");
            }

            System.out.println("\nDevam etmek için Enter'a bas...");
            scanner.nextLine();
        }
    }

    public static void tumunuListele(String dosyaAdi) {
        try {
            Scanner oku = new Scanner(new File(dosyaAdi), "UTF-8");
            System.out.println("--- Tüm Karakterler ---");
            while (oku.hasNextLine()) {
                System.out.println(oku.nextLine());
            }
            oku.close();
        } catch (Exception e) {
            System.out.println("Dosya okunamadı.");
        }
    }

    public static void filtrele(String dosyaAdi, String aranan, int kolonIndex) {
        try {
            Scanner oku = new Scanner(new File(dosyaAdi), "UTF-8");
            System.out.println("--- Filtre Sonuçları ---");
            while (oku.hasNextLine()) {
                String satir = oku.nextLine();
                String[] parcalar = satir.split(";");
                if (parcalar.length > kolonIndex && parcalar[kolonIndex].equalsIgnoreCase(aranan)) {
                    System.out.println(satir);
                }
            }
            oku.close();
        } catch (Exception e) {
            System.out.println("Dosya okunamadı.");
        }
    }

    public static void favorileriListele() {
        try {
            Scanner oku = new Scanner(new File("favoriler.txt"), "UTF-8");
            System.out.println("--- Favori Karakterler ---");
            while (oku.hasNextLine()) {
                System.out.println(oku.nextLine());
            }
            oku.close();
        } catch (FileNotFoundException e) {
            System.out.println("Favoriler dosyası bulunamadı.");
        }
    }

    public static void dosyaYedekle(String kaynakDosya) {
        String yedekDosya = "karakterler_yedek.txt";
        try {
            Files.copy(Paths.get(kaynakDosya), Paths.get(yedekDosya), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Yedekleme tamamlandı: " + yedekDosya);
        } catch (IOException e) {
            System.out.println("Yedekleme sırasında hata: " + e.getMessage());
        }
    }
        public static void guncelle(String dosyaAdi, Scanner scanner) {
        System.out.print("Güncellenecek karakterin adını girin: ");
        String arananAd = scanner.nextLine();

        File dosya = new File(dosyaAdi);
        File tempDosya = new File("gecici.txt");

        try {
            Scanner oku = new Scanner(dosya, "UTF-8");
            BufferedWriter yazici = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempDosya), "UTF-8"));
            boolean bulundu = false;

            while (oku.hasNextLine()) {
                String satir = oku.nextLine();
                String[] parcalar = satir.split(";");
                if (parcalar[0].equalsIgnoreCase(arananAd)) {
                    System.out.println("Bulunan kayıt: " + satir);
                    System.out.print("Güncellemek istiyor musunuz? (e/h): ");
                    String onay = scanner.nextLine();
                    if (onay.equalsIgnoreCase("e")) {
                        System.out.print("Yeni ev: ");
                        String yeniEv = scanner.nextLine();
                        System.out.print("Yeni patronus: ");
                        String yeniPatronus = scanner.nextLine();
                        System.out.print("Yeni doğum tarihi: ");
                        String yeniDogum = scanner.nextLine();
                        System.out.print("Öğrenci mi? (e/h): ");
                        String ogrenciMi = scanner.nextLine().equalsIgnoreCase("e") ? "Öğrenci" : "Değil";

                        String yeniSatir = parcalar[0] + ";" + yeniEv + ";" + yeniPatronus + ";" + yeniDogum + ";" + ogrenciMi + "\n";
                        yazici.write(yeniSatir);
                        bulundu = true;
                        continue;
                    }
                }
                yazici.write(satir + "\n");
            }

            oku.close();
            yazici.close();

            if (dosya.delete() && tempDosya.renameTo(dosya)) {
                System.out.println(bulundu ? "Kayıt güncellendi." : "Kayıt bulunamadı.");
            } else {
                System.out.println("Dosya güncellenemedi.");
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    public static void sil(String dosyaAdi, Scanner scanner) {
        System.out.print("Silinecek karakterin adını girin: ");
        String arananAd = scanner.nextLine();

        File dosya = new File(dosyaAdi);
        File tempDosya = new File("gecici.txt");

        try {
            Scanner oku = new Scanner(dosya, "UTF-8");
            BufferedWriter yazici = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempDosya), "UTF-8"));
            boolean silindi = false;

            while (oku.hasNextLine()) {
                String satir = oku.nextLine();
                String[] parcalar = satir.split(";");
                if (parcalar[0].equalsIgnoreCase(arananAd)) {
                    System.out.println("Bulunan kayıt: " + satir);
                    System.out.print("Silmek istiyor musunuz? (e/h): ");
                    String onay = scanner.nextLine();
                    if (onay.equalsIgnoreCase("e")) {
                        silindi = true;
                        continue;
                    }
                }
                yazici.write(satir + "\n");
            }

            oku.close();
            yazici.close();

            if (dosya.delete() && tempDosya.renameTo(dosya)) {
                System.out.println(silindi ? "Kayıt silindi." : "Kayıt bulunamadı.");
            } else {
                System.out.println("Dosya silinemedi.");
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    public static void favoriyeEkle(String dosyaAdi, Scanner scanner) {
        System.out.print("Favoriye eklemek istediğiniz karakterin adı: ");
        String arananAd = scanner.nextLine();
        boolean bulundu = false;

        try {
            Scanner oku = new Scanner(new File(dosyaAdi), "UTF-8");
            BufferedWriter favYazici = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("favoriler.txt", true), "UTF-8"));
            while (oku.hasNextLine()) {
                String satir = oku.nextLine();
                if (satir.toLowerCase().startsWith(arananAd.toLowerCase() + ";")) {
                    favYazici.write(satir + "\n");
                    bulundu = true;
                    break;
                }
            }
            oku.close();
            favYazici.close();
            System.out.println(bulundu ? "Favorilere eklendi." : "Karakter bulunamadı.");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    public static String araVeAl(String metin, String bas, String son) {
        int basIndex = metin.indexOf(bas);
        if (basIndex == -1) return "Yok";
        basIndex += bas.length();
        int sonIndex = metin.indexOf(son, basIndex);
        if (sonIndex == -1) return "Yok";
        return metin.substring(basIndex, sonIndex);
    }
}

