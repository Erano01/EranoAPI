package me.erano.com.api.particle;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Parçacık sistemi için temel sağlayıcı arayüzü.
 * Her NMS sürümü bu arayüzü implement etmelidir.
 */
public interface IParticleProvider {
    /**
     * Belirtilen konumda bir parçacık oluşturur.
     * 
     * @param world hedef dünya
     * @param location parçacığın oluşturulacağı konum
     * @param particle parçacık türü
     * @param count oluşturulacak parçacık sayısı
     * @param offsetX x ekseni sapması
     * @param offsetY y ekseni sapması
     * @param offsetZ z ekseni sapması
     * @param speed parçacık hızı
     * @param data özel veri (blok veya item)
     * @param receivers parçacığı görecek oyuncular (null ise tüm oyuncular görür)
     */
    void spawnParticle(World world, Location location, ParticleEffect particle, 
                      int count, double offsetX, double offsetY, double offsetZ,
                      double speed, Object data, Player... receivers);

    /**
     * Parçacık efektinin sunucu sürümünde desteklenip desteklenmediğini kontrol eder.
     * 
     * @param particle kontrol edilecek parçacık türü
     * @return parçacık destekleniyorsa true
     */
    boolean isSupported(ParticleEffect particle);

    /**
     * Sürüm kontrolü yapar.
     * 
     * @param version kontrol edilecek sürüm (örn: "1.8", "1.12.2")
     * @return bu sağlayıcı verilen sürümü destekliyorsa true
     */
    boolean supportsVersion(String version);
}
