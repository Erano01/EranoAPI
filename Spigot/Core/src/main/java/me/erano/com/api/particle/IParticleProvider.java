package me.erano.com.api.particle;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.erano.com.common.VersionedService;

/**
 * Parçacık sistemi için temel sağlayıcı arayüzü.
 * 1.9+ için Bukkit API ({@link BukkitParticleProvider}), 1.8 için NMS uygulamaları.
 */
public interface IParticleProvider extends VersionedService {
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
}
