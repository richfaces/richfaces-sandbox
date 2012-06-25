package org.richfaces.sandbox.composite.demo; /**
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 **/

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ManagedBean
@RequestScoped
public class GalleryBean {
    private List<FlickrPhoto> photos;

    public GalleryBean() {
        initialize();
    }

    public List<FlickrPhoto> getPhotos() {
        return photos;
    }

    public void initialize() {
        photos = new ArrayList<FlickrPhoto>();
        photos.add(new FlickrPhoto("", "http://farm8.static.flickr.com/7095/7403727842_26b23ef426_b.jpg", "http://farm8.static.flickr.com/7095/7403727842_26b23ef426_s.jpg"));
        photos.add(new FlickrPhoto("Fairy tale castle", "http://farm8.static.flickr.com/7251/7408636642_525fd8c8bd_b.jpg", "http://farm8.static.flickr.com/7251/7408636642_525fd8c8bd_s.jpg"));
        photos.add(new FlickrPhoto("Fall In Colorado-Explored #47 6/20", "http://farm8.static.flickr.com/7234/7405743722_5026589fe6_b.jpg", "http://farm8.static.flickr.com/7234/7405743722_5026589fe6_s.jpg"));
        photos.add(new FlickrPhoto("Country Road Dreamin'  ~ Explore ~", "http://farm8.static.flickr.com/7115/7408577232_97498d5835_b.jpg", "http://farm8.static.flickr.com/7115/7408577232_97498d5835_s.jpg"));
        photos.add(new FlickrPhoto("Cupid's Arrow", "http://farm8.static.flickr.com/7137/7408802932_7f69e246e0_b.jpg", "http://farm8.static.flickr.com/7137/7408802932_7f69e246e0_s.jpg"));
        photos.add(new FlickrPhoto("light and shadows", "http://farm8.static.flickr.com/7270/7409392124_91a9a8b9df_b.jpg", "http://farm8.static.flickr.com/7270/7409392124_91a9a8b9df_s.jpg"));
        photos.add(new FlickrPhoto("Bending Light (Upper Antelope Canyon)", "http://farm8.static.flickr.com/7111/7411602600_6cf59e0aa1_b.jpg", "http://farm8.static.flickr.com/7111/7411602600_6cf59e0aa1_s.jpg"));
        photos.add(new FlickrPhoto("", "http://farm6.static.flickr.com/5470/7409033658_94c7d84386_b.jpg", "http://farm6.static.flickr.com/5470/7409033658_94c7d84386_s.jpg"));
        photos.add(new FlickrPhoto("Storm Over Hayden Valley, III ~ explored 6/20/2012", "http://farm8.static.flickr.com/7249/7408988662_b71efd7027_b.jpg", "http://farm8.static.flickr.com/7249/7408988662_b71efd7027_s.jpg"));
        photos.add(new FlickrPhoto("Porz guen ---- Explore #45", "http://farm9.static.flickr.com/8143/7409530206_39bc1d9620_b.jpg", "http://farm9.static.flickr.com/8143/7409530206_39bc1d9620_s.jpg"));
        photos.add(new FlickrPhoto("#EXPLORE", "http://farm6.static.flickr.com/5117/7410370290_0935419fc3_b.jpg", "http://farm6.static.flickr.com/5117/7410370290_0935419fc3_s.jpg"));
        photos.add(new FlickrPhoto("Eifel-Summer 2)", "http://farm6.static.flickr.com/5455/7408802926_cf8fdd5d23_b.jpg", "http://farm6.static.flickr.com/5455/7408802926_cf8fdd5d23_s.jpg"));
        photos.add(new FlickrPhoto("Milky Ways", "http://farm9.static.flickr.com/8153/7410776014_dab773700f_b.jpg", "http://farm9.static.flickr.com/8153/7410776014_dab773700f_s.jpg"));
        photos.add(new FlickrPhoto("Wind, Water, and Fire; Its All You Need", "http://farm8.static.flickr.com/7128/7409154054_cef53dd0c2_b.jpg", "http://farm8.static.flickr.com/7128/7409154054_cef53dd0c2_s.jpg"));
        photos.add(new FlickrPhoto("", "http://farm8.static.flickr.com/7256/7408464426_f5c030433f_b.jpg", "http://farm8.static.flickr.com/7256/7408464426_f5c030433f_s.jpg"));
        photos.add(new FlickrPhoto("Llyn Padarn Study 2", "http://farm9.static.flickr.com/8008/7409615988_471a4219cd_b.jpg", "http://farm9.static.flickr.com/8008/7409615988_471a4219cd_s.jpg"));
        photos.add(new FlickrPhoto("Roca", "http://farm8.static.flickr.com/7074/7400382668_d4901cf549_b.jpg", "http://farm8.static.flickr.com/7074/7400382668_d4901cf549_s.jpg"));
        photos.add(new FlickrPhoto("Wedding : Khánh ♥ Mai Anh (Explore)", "http://farm8.static.flickr.com/7261/7409017920_0d90d3ab89_b.jpg", "http://farm8.static.flickr.com/7261/7409017920_0d90d3ab89_s.jpg"));
        photos.add(new FlickrPhoto("If You Gotta Go, You Gotta Go..", "http://farm6.static.flickr.com/5234/7413552804_069819e393_b.jpg", "http://farm6.static.flickr.com/5234/7413552804_069819e393_s.jpg"));
        photos.add(new FlickrPhoto("High Coast", "http://farm9.static.flickr.com/8165/7410324438_296fe53d32_b.jpg", "http://farm9.static.flickr.com/8165/7410324438_296fe53d32_s.jpg"));
        photos.add(new FlickrPhoto("Push it real good (The City)", "http://farm6.static.flickr.com/5196/7409655934_2ac591925e_b.jpg", "http://farm6.static.flickr.com/5196/7409655934_2ac591925e_s.jpg"));
        photos.add(new FlickrPhoto("Untitled (EXPLORE FRONT PAGE)", "http://farm8.static.flickr.com/7140/7408930864_e5d6520c19_b.jpg", "http://farm8.static.flickr.com/7140/7408930864_e5d6520c19_s.jpg"));
        photos.add(new FlickrPhoto("Rex Ray inspired. Pastiche bee ~ block for July (1)", "http://farm9.static.flickr.com/8008/7412817672_5d7118e6aa_b.jpg", "http://farm9.static.flickr.com/8008/7412817672_5d7118e6aa_s.jpg"));
        photos.add(new FlickrPhoto("Follow the path..", "http://farm9.static.flickr.com/8161/7409863552_a5ee2a2b3c_b.jpg", "http://farm9.static.flickr.com/8161/7409863552_a5ee2a2b3c_s.jpg"));
        photos.add(new FlickrPhoto("Stand Naked and Accused [Explored #86]", "http://farm6.static.flickr.com/5276/7409315912_1578c32ab1_b.jpg", "http://farm6.static.flickr.com/5276/7409315912_1578c32ab1_s.jpg"));
        photos.add(new FlickrPhoto("old railway bridge Dömitz / Explore", "http://farm6.static.flickr.com/5459/7409000652_cf4846022a_b.jpg", "http://farm6.static.flickr.com/5459/7409000652_cf4846022a_s.jpg"));
        photos.add(new FlickrPhoto("Meet me in the city", "http://farm8.static.flickr.com/7274/7411735412_289dd788a4_b.jpg", "http://farm8.static.flickr.com/7274/7411735412_289dd788a4_s.jpg"));
        photos.add(new FlickrPhoto("DSC_1298 made Explore", "http://farm8.static.flickr.com/7130/7410421316_7688748706_b.jpg", "http://farm8.static.flickr.com/7130/7410421316_7688748706_s.jpg"));
        photos.add(new FlickrPhoto("Pony Reflections", "http://farm8.static.flickr.com/7130/7410354906_111aacaef1_b.jpg", "http://farm8.static.flickr.com/7130/7410354906_111aacaef1_s.jpg"));
        photos.add(new FlickrPhoto("Kría (Sterna paradisaea)", "http://farm8.static.flickr.com/7253/7410108390_c1e3279184_b.jpg", "http://farm8.static.flickr.com/7253/7410108390_c1e3279184_s.jpg"));
        photos.add(new FlickrPhoto("Blue tit", "http://farm8.static.flickr.com/7136/7412282108_628129de5b_b.jpg", "http://farm8.static.flickr.com/7136/7412282108_628129de5b_s.jpg"));
        photos.add(new FlickrPhoto("summer whispers", "http://farm6.static.flickr.com/5466/7410142474_758a04da63_b.jpg", "http://farm6.static.flickr.com/5466/7410142474_758a04da63_s.jpg"));
        photos.add(new FlickrPhoto("Fence @ Sunset", "http://farm8.static.flickr.com/7268/7414148272_159d205465_b.jpg", "http://farm8.static.flickr.com/7268/7414148272_159d205465_s.jpg"));
        photos.add(new FlickrPhoto("Lara ♥", "http://farm8.static.flickr.com/7129/7410564238_89831162a1_b.jpg", "http://farm8.static.flickr.com/7129/7410564238_89831162a1_s.jpg"));
        photos.add(new FlickrPhoto("Aung San Suu Kyi in Oxford", "http://farm9.static.flickr.com/8005/7409267164_595bbcb23d_b.jpg", "http://farm9.static.flickr.com/8005/7409267164_595bbcb23d_s.jpg"));
        photos.add(new FlickrPhoto("In the gloaming at Hopeman", "http://farm6.static.flickr.com/5279/7410109584_d351b3d0b1_b.jpg", "http://farm6.static.flickr.com/5279/7410109584_d351b3d0b1_s.jpg"));
        photos.add(new FlickrPhoto("Wroclaw/Out of Sth vol.3", "http://farm9.static.flickr.com/8008/7413071784_c94228d231_b.jpg", "http://farm9.static.flickr.com/8008/7413071784_c94228d231_s.jpg"));
        photos.add(new FlickrPhoto("AttrActiOn   ((explore))", "http://farm9.static.flickr.com/8144/7412620736_a1afe84e22_b.jpg", "http://farm9.static.flickr.com/8144/7412620736_a1afe84e22_s.jpg"));
        photos.add(new FlickrPhoto("Playa de Bascuas", "http://farm9.static.flickr.com/8024/7409165806_e81128c99c_b.jpg", "http://farm9.static.flickr.com/8024/7409165806_e81128c99c_s.jpg"));
        photos.add(new FlickrPhoto("built in 1899 [Explore]", "http://farm9.static.flickr.com/8011/7409013424_1db330b584_b.jpg", "http://farm9.static.flickr.com/8011/7409013424_1db330b584_s.jpg"));
        photos.add(new FlickrPhoto("aroma", "http://farm9.static.flickr.com/8007/7412726260_0d83426560_b.jpg", "http://farm9.static.flickr.com/8007/7412726260_0d83426560_s.jpg"));
        photos.add(new FlickrPhoto("sundance 1", "http://farm8.static.flickr.com/7130/7409580644_acd543b88b_b.jpg", "http://farm8.static.flickr.com/7130/7409580644_acd543b88b_s.jpg"));
        photos.add(new FlickrPhoto("Aguila Americana nº3", "http://farm8.static.flickr.com/7280/7413198066_1b3f51b1e2_b.jpg", "http://farm8.static.flickr.com/7280/7413198066_1b3f51b1e2_s.jpg"));
        photos.add(new FlickrPhoto("#(Explore)&quot;insects&quot; Project  4/6", "http://farm8.static.flickr.com/7269/7410481596_b6a5136733_b.jpg", "http://farm8.static.flickr.com/7269/7410481596_b6a5136733_s.jpg"));
        photos.add(new FlickrPhoto("paisagem - landscape - fotógrafo Marcos Arruda", "http://farm8.static.flickr.com/7139/7408670618_877bc04613_b.jpg", "http://farm8.static.flickr.com/7139/7408670618_877bc04613_s.jpg"));
        photos.add(new FlickrPhoto("Distant Meteroar", "http://farm6.static.flickr.com/5466/7409497182_2826b1da53_b.jpg", "http://farm6.static.flickr.com/5466/7409497182_2826b1da53_s.jpg"));
        photos.add(new FlickrPhoto("Combe Martin Bandstand", "http://farm9.static.flickr.com/8158/7408457930_c9c6a3d8ed_b.jpg", "http://farm9.static.flickr.com/8158/7408457930_c9c6a3d8ed_s.jpg"));
        photos.add(new FlickrPhoto("Below Wholesale", "http://farm8.static.flickr.com/7124/7411033454_88e72ba8bf_b.jpg", "http://farm8.static.flickr.com/7124/7411033454_88e72ba8bf_s.jpg"));
        photos.add(new FlickrPhoto("Hawt!", "http://farm6.static.flickr.com/5456/7408289446_0c2d3bf6f7_b.jpg", "http://farm6.static.flickr.com/5456/7408289446_0c2d3bf6f7_s.jpg"));
        photos.add(new FlickrPhoto("The variety....", "http://farm6.static.flickr.com/5346/7409631360_953baabc9a_b.jpg", "http://farm6.static.flickr.com/5346/7409631360_953baabc9a_s.jpg"));
        photos.add(new FlickrPhoto("untitled", "http://farm8.static.flickr.com/7121/7409884082_05e49105a1_b.jpg", "http://farm8.static.flickr.com/7121/7409884082_05e49105a1_s.jpg"));
        photos.add(new FlickrPhoto("Moreno Glacier", "http://farm9.static.flickr.com/8168/7408594268_4ca4548caf_b.jpg", "http://farm9.static.flickr.com/8168/7408594268_4ca4548caf_s.jpg"));
        photos.add(new FlickrPhoto("Madonna MDNA Concert (Explore #19) _D7C31354", "http://farm8.static.flickr.com/7279/7406721864_778725baf9_b.jpg", "http://farm8.static.flickr.com/7279/7406721864_778725baf9_s.jpg"));
        photos.add(new FlickrPhoto("Two", "http://farm6.static.flickr.com/5040/7414005506_3daa4b813e_b.jpg", "http://farm6.static.flickr.com/5040/7414005506_3daa4b813e_s.jpg"));
        photos.add(new FlickrPhoto("Minimalistic detail of an art object at the museum Haags Gemeentemuseum, The Hague, The Netherlands", "http://farm9.static.flickr.com/8144/7409809502_fa737a1ef9_b.jpg", "http://farm9.static.flickr.com/8144/7409809502_fa737a1ef9_s.jpg"));
        photos.add(new FlickrPhoto("Renardeau Espiègle - Playful fox", "http://farm6.static.flickr.com/5462/7409665738_9aa1af7bc0_b.jpg", "http://farm6.static.flickr.com/5462/7409665738_9aa1af7bc0_s.jpg"));
        photos.add(new FlickrPhoto("No. 1", "http://farm9.static.flickr.com/8020/7409422192_1689e65333_b.jpg", "http://farm9.static.flickr.com/8020/7409422192_1689e65333_s.jpg"));
        photos.add(new FlickrPhoto("Jasik Robot", "http://farm8.static.flickr.com/7136/7408640302_8736692935_b.jpg", "http://farm8.static.flickr.com/7136/7408640302_8736692935_s.jpg"));
        photos.add(new FlickrPhoto("Rainham Heron", "http://farm8.static.flickr.com/7113/7409825940_3a0f88c1c9_b.jpg", "http://farm8.static.flickr.com/7113/7409825940_3a0f88c1c9_s.jpg"));
        photos.add(new FlickrPhoto("", "http://farm8.static.flickr.com/7251/7414230560_bd82209b9f_b.jpg", "http://farm8.static.flickr.com/7251/7414230560_bd82209b9f_s.jpg"));
        photos.add(new FlickrPhoto("it's all about, the Barbed Wire Fence", "http://farm8.static.flickr.com/7272/7410041496_280526bc45_b.jpg", "http://farm8.static.flickr.com/7272/7410041496_280526bc45_s.jpg"));
        photos.add(new FlickrPhoto("Azkenean... uda! ¡Por fín... el verano!", "http://farm6.static.flickr.com/5457/7409658082_45c77811bd_b.jpg", "http://farm6.static.flickr.com/5457/7409658082_45c77811bd_s.jpg"));
        photos.add(new FlickrPhoto("Gold Aventador AU79", "http://farm9.static.flickr.com/8155/7412736192_91e92452d5_b.jpg", "http://farm9.static.flickr.com/8155/7412736192_91e92452d5_s.jpg"));
        photos.add(new FlickrPhoto("IMG_0806   Yellow Lilly  Explored - #23", "http://farm9.static.flickr.com/8165/7404294102_1ef00c1fd2_b.jpg", "http://farm9.static.flickr.com/8165/7404294102_1ef00c1fd2_s.jpg"));
        photos.add(new FlickrPhoto("Black Throated Diver, at sea", "http://farm9.static.flickr.com/8001/7414158260_9b399d919d_b.jpg", "http://farm9.static.flickr.com/8001/7414158260_9b399d919d_s.jpg"));
        photos.add(new FlickrPhoto("Morning view with ground fog", "http://farm6.static.flickr.com/5112/7413500660_d1e8f62fef_b.jpg", "http://farm6.static.flickr.com/5112/7413500660_d1e8f62fef_s.jpg"));
        photos.add(new FlickrPhoto("Kupu-Kupu", "http://farm9.static.flickr.com/8025/7412668522_c69616a133_b.jpg", "http://farm9.static.flickr.com/8025/7412668522_c69616a133_s.jpg"));
        photos.add(new FlickrPhoto("“A ship is safe in harbor, but that's not what ships are for.”", "http://farm9.static.flickr.com/8156/7410243892_1bf2107efe_b.jpg", "http://farm9.static.flickr.com/8156/7410243892_1bf2107efe_s.jpg"));
        photos.add(new FlickrPhoto("FE180 - Explored", "http://farm8.static.flickr.com/7279/7410188576_e9d9d413ea_b.jpg", "http://farm8.static.flickr.com/7279/7410188576_e9d9d413ea_s.jpg"));
        photos.add(new FlickrPhoto("", "http://farm6.static.flickr.com/5466/7409019954_2236057126_b.jpg", "http://farm6.static.flickr.com/5466/7409019954_2236057126_s.jpg"));
        photos.add(new FlickrPhoto("Sea Sand Spray 2012", "http://farm9.static.flickr.com/8005/7406284060_23b2531a0d_b.jpg", "http://farm9.static.flickr.com/8005/7406284060_23b2531a0d_s.jpg"));
        photos.add(new FlickrPhoto("Excursión a La Gomera", "http://farm6.static.flickr.com/5453/7408899892_a4b4d3f23c_b.jpg", "http://farm6.static.flickr.com/5453/7408899892_a4b4d3f23c_s.jpg"));
        photos.add(new FlickrPhoto("A sense of place", "http://farm9.static.flickr.com/8145/7409346510_931e7a9d86_b.jpg", "http://farm9.static.flickr.com/8145/7409346510_931e7a9d86_s.jpg"));
        photos.add(new FlickrPhoto("intermezzo", "http://farm9.static.flickr.com/8023/7413133442_1bc5648052_b.jpg", "http://farm9.static.flickr.com/8023/7413133442_1bc5648052_s.jpg"));
        photos.add(new FlickrPhoto("Terrace sparrows 3", "http://farm9.static.flickr.com/8014/7408822428_7e58ebccdc_b.jpg", "http://farm9.static.flickr.com/8014/7408822428_7e58ebccdc_s.jpg"));
        photos.add(new FlickrPhoto("Sitting on a rock in Woodford (Explored)", "http://farm8.static.flickr.com/7260/7411081840_43d83f7b70_b.jpg", "http://farm8.static.flickr.com/7260/7411081840_43d83f7b70_s.jpg"));
        photos.add(new FlickrPhoto("Eagle Vision - View to see details", "http://farm8.static.flickr.com/7134/7412655720_0d70c66be2_b.jpg", "http://farm8.static.flickr.com/7134/7412655720_0d70c66be2_s.jpg"));
        photos.add(new FlickrPhoto("&quot;I Am...&quot; a Pilot", "http://farm8.static.flickr.com/7119/7410327952_dda8b969f8_b.jpg", "http://farm8.static.flickr.com/7119/7410327952_dda8b969f8_s.jpg"));
        photos.add(new FlickrPhoto("dos", "http://farm9.static.flickr.com/8148/7409333430_f606386bfa_b.jpg", "http://farm9.static.flickr.com/8148/7409333430_f606386bfa_s.jpg"));
        photos.add(new FlickrPhoto("IMG_5896 Moros i Cristianos 3 - be amazed - se sorprenderá - (Street shot - Moraira 2012)   - Seen On Explore - 2012-06-20 #27", "http://farm8.static.flickr.com/7258/7408263848_2cbcef7678_b.jpg", "http://farm8.static.flickr.com/7258/7408263848_2cbcef7678_s.jpg"));
        photos.add(new FlickrPhoto("Spool Pillow Top", "http://farm9.static.flickr.com/8009/7409645656_a0dc271b32_b.jpg", "http://farm9.static.flickr.com/8009/7409645656_a0dc271b32_s.jpg"));
        photos.add(new FlickrPhoto("because it`s still wednesday", "http://farm9.static.flickr.com/8166/7409344266_a37e8ed5c3_b.jpg", "http://farm9.static.flickr.com/8166/7409344266_a37e8ed5c3_s.jpg"));
        photos.add(new FlickrPhoto("Ice Cream Phone Cards", "http://farm6.static.flickr.com/5321/7412706176_89d4f57ee3_b.jpg", "http://farm6.static.flickr.com/5321/7412706176_89d4f57ee3_s.jpg"));
        photos.add(new FlickrPhoto("Magpie twilight~Explored", "http://farm8.static.flickr.com/7269/7412469646_07f3062746_b.jpg", "http://farm8.static.flickr.com/7269/7412469646_07f3062746_s.jpg"));
        photos.add(new FlickrPhoto("B 2502 a little boy from the past ( EXPLORE!!  21 JUNE )", "http://farm6.static.flickr.com/5441/7411573464_6146f22b39_b.jpg", "http://farm6.static.flickr.com/5441/7411573464_6146f22b39_s.jpg"));
        photos.add(new FlickrPhoto("los pájaros no necesitan murallas/ birds do not need walls {explore}", "http://farm6.static.flickr.com/5031/7410519410_4e19809ab5_b.jpg", "http://farm6.static.flickr.com/5031/7410519410_4e19809ab5_s.jpg"));
        photos.add(new FlickrPhoto("Waiting", "http://farm9.static.flickr.com/8023/7395524964_74efc81886_b.jpg", "http://farm9.static.flickr.com/8023/7395524964_74efc81886_s.jpg"));
        photos.add(new FlickrPhoto("GESA", "http://farm9.static.flickr.com/8148/7413076356_73bdc9e21e_b.jpg", "http://farm9.static.flickr.com/8148/7413076356_73bdc9e21e_s.jpg"));
        photos.add(new FlickrPhoto("The old window", "http://farm9.static.flickr.com/8154/7411184920_08509414bd_b.jpg", "http://farm9.static.flickr.com/8154/7411184920_08509414bd_s.jpg"));
        photos.add(new FlickrPhoto("Aspekt/Spyok", "http://farm8.static.flickr.com/7269/7410286158_01499ff220_b.jpg", "http://farm8.static.flickr.com/7269/7410286158_01499ff220_s.jpg"));
        photos.add(new FlickrPhoto("", "http://farm8.static.flickr.com/7269/7409263506_caac8fd291_b.jpg", "http://farm8.static.flickr.com/7269/7409263506_caac8fd291_s.jpg"));
        photos.add(new FlickrPhoto("black lady , explored!", "http://farm9.static.flickr.com/8148/7414135018_95a20f3bbf_b.jpg", "http://farm9.static.flickr.com/8148/7414135018_95a20f3bbf_s.jpg"));
        photos.add(new FlickrPhoto("Peony x10", "http://farm6.static.flickr.com/5194/7413297022_ac3b893eb2_b.jpg", "http://farm6.static.flickr.com/5194/7413297022_ac3b893eb2_s.jpg"));
        photos.add(new FlickrPhoto("by the lake", "http://farm9.static.flickr.com/8162/7409999952_b40508044e_b.jpg", "http://farm9.static.flickr.com/8162/7409999952_b40508044e_s.jpg"));
        photos.add(new FlickrPhoto("illusions (explore)", "http://farm8.static.flickr.com/7233/7399382438_1fa2e4ca85_b.jpg", "http://farm8.static.flickr.com/7233/7399382438_1fa2e4ca85_s.jpg"));
        photos.add(new FlickrPhoto("Cada uno cosecha lo que siembra...", "http://farm6.static.flickr.com/5342/7413639612_69960f5745_b.jpg", "http://farm6.static.flickr.com/5342/7413639612_69960f5745_s.jpg"));
        photos.add(new FlickrPhoto("Reflections (Explored)", "http://farm8.static.flickr.com/7123/7408745106_ba1050be92_b.jpg", "http://farm8.static.flickr.com/7123/7408745106_ba1050be92_s.jpg"));
        photos.add(new FlickrPhoto("", "http://farm8.static.flickr.com/7253/7408640106_22fd20af6c_b.jpg", "http://farm8.static.flickr.com/7253/7408640106_22fd20af6c_s.jpg"));
        photos.add(new FlickrPhoto("Venezia", "http://farm6.static.flickr.com/5326/7413244004_9c201953cd_b.jpg", "http://farm6.static.flickr.com/5326/7413244004_9c201953cd_s.jpg"));
        photos.add(new FlickrPhoto("Fur Tor, dried stream", "http://farm9.static.flickr.com/8154/7409980460_03a7372240_b.jpg", "http://farm9.static.flickr.com/8154/7409980460_03a7372240_s.jpg"));
    }
}
