/*
 Copyright 2015 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/
package dibl

import dibl.Matrix.{charToRelativeTuples, extend}

object Matrices {
  val values = Array(
    "5831 -4-7;bricks", "1483 8-48;bricks", "5831 -4-7", "6868 -4-4 2121 -7-7;bricks", "4815 4-77;bricks", "486- -486 6-48 86-4", "4641 9177;bricks",
    "6868 ---4 2AA1 -7-7;bricks", "5831 -4-7 6868 -4-4", "586- -4-5 2121 -7-7", "486- -486 2111 88-7", "5632 56-2 6-58 -534", "586- -4-5 215- -7-5",
    "4373 5353 5-5- 8315", "434- 6325 6-25 8686", "6868 -4-4 5-5- -5-5", "6464 7272;bricks", "4343 6868;bricks", "4663 6668;bricks",
    "5-L-5-E- -E-5-5-O 5-O-H-5- -5-5-5-K;bricks", "5-O-5-O- -E-5-E-5 5-H-5-H- -L-5-L-5;bricks", "486- -486 5-4- 86-5", "5631 66-7;bricks", "588- -115",
    "6868 ---- AAAA -7-7;bricks", "66 -4 21 88;bricks", "6868 1111 8888 -4-4", "88 11;bricks", "66 88 66 11;bricks", "53 53 53 5-;bricks", "5- -5",
    "588- 14-2;bricks", "4831 -488 214- 88-5", "4831 -4-7 3158 88-4", "4831 -117 3178 88-4", "586- -4-5 5-21 -5-7", "4312 6-78;bricks",
    "486- -115 217- 88-5", "4831 -117 6-78 86-4", "4831 -117 5-7- 86-5", "4831 -488 6-48 86-4", "4831 -488 5-4- 86-5", "588- -4-5 6-58 -214",
    "586- -115 6-78 -5-4", "5831 -4-7 6-58 -5-4", "4831 -4-7 215- 88-5", "4831 -4-7 6-58 86-4", "4832 2483;bricks", "43 21 -4 98;bricks",
    "5-L---H- -L-O-L-O --5---5- -E-H-E-H;bricks", "5-L---H- -L-O-L-O --5-K-5- -E-E-H-H;bricks", "5831 -4-7 3158 -7-4;bricks",
    "466- 6315 6-76 8666;bricks", "4631 66-7 6-56 8666;bricks", "4631 6688 6-46 8666;bricks", "4631 6688 3146 8866;bricks",
    "586- 1112 8-78 -5-4;bricks", "588- -4-5 6868 -114", "586- ---5 2AB- -7-5;bricks", "586- ---5 2AA1 -7-7;bricks", "5-O-H- -L-5-O E-5-E-;bricks",
    "586- -4-5 215- -7-5;bricks", "466- 6315 6-76 8666", "588- 1112 8-78 -214", "586- 1112 8-78 -5-4", "6888 14-1 8868 -114", "588- 14-2 8868 -114",
    "588- -115 6-78 -214", "4631 6688 3146 8866", "4631 6688;bricks", "4631 66-7 3156 8866;bricks", "466- 66-5 6-56 8666;bricks",
    "466- 6686 6-46 8666;bricks", "4631 6317 6-76 8666;bricks", "4631 6317 3176 8866;bricks", "6868 -114 6888 -4-4", "4631 6688 6-46 8666",
    "4631 6317 6-76 8666", "4631 6317 3176 8866", "586- 1112 8888 -4-4", "4343 5353 5-21 8688", "6868 88-7 1121 -4-4", "586- 8889 1111 -4-4",
    "586- -115 6888 -4-4", "586- -115 588- -4-5", "586- -115 5-7- -5-5", "586- 1112 788- -4-5", "586- 1112 7-7- -5-5", "586- -789 2111 -4-4",
    "4632 5683 6-48 8634", "6888 8888 4-11 -014", "588- 1112 8888 -114", "588- -115 6888 -114", "6888 1111 8888 -114", "6888 88-7 1121 -114",
    "6888 -788 2111 -114", "4632 5683 5-11 8637", "588- 8889 4-11 -014", "4632 5683 3148 8834", "588- -789 5-11 -014", "588- -789 2111 -114",
    "4632 5683 214- 8835", "4632 5683 2111 8837", "4632 56-2 6-58 8634", "4632 56-2 5-21 8637", "4632 56-2 2121 8837", "4632 5312 6-78 8634",
    "4632 5312 5-7- 8635", "4632 5312 3178 8834", "5-H-H- -5-H-H 5-L-O-;bricks", "5-E-H- -5-5-- L-5-O-;bricks", "5-O-H- -E-5-H 5-L-L-;bricks",
    "5-L-H- -L-5-O E-H-5-;bricks", "5-E-H- -5-O-O H-H-5-;bricks", "486- -486 5-11 86-7", "486- -486 214- 88-5", "4832 2483 224- 8325",
    "486- -4-5 5-5- 86-5", "586- -4-5 5-5- -5-5", "4832 24-2 225- 8325", "4831 -4-7 5-5- 86-5", "486- -4-5 215- 88-5", "4831 -4-7 5-21 86-7",
    "486- -4-5 5-21 86-7", "486- -4-5 2121 88-7", "4831 -4-7 2121 88-7", "4831 3437 3535 86-5", "4353 5353 6-58 86-4", "4353 5353 5-21 86-7",
    "4632 5683 5-4- 8635", "466- 66-5 6-56 8666", "5631 66-7 6-56 -566", "4631 66-7 6-56 8666", "4631 66-7 3156 8866", "4632 56-2 3158 8834",
    "4632 56-2 215- 8835", "4321 5883;bricks", "4353 5863;bricks", "4311 6888;bricks", "68 4- -5 5-;bricks", "43 68 34 86;bricks", "43 5- 35 86;bricks",
    "68 -4 21 7-;bricks", "46-1 6868;bricks", "4863 5663;bricks", "46-2 6-58;bricks", "48-2 5-53;bricks", "8464 7712;bricks", "4466 7781;bricks",
    "4683 3468;bricks", "4683 6-48;bricks", "4632 3488;bricks", "4840 5887;bricks", "4883 5-43;bricks", "4488 1748;bricks",
    "4343 5353 5-21 8688;bricks", "586- -4-5 5-21 -5-7;bricks", "6868 -114 6888 -4-4;bricks", "586- -4-5 5-5- -5-5;bricks",
    "586- -789 2111 -4-4;bricks", "586- 8889 1111 -4-4;bricks", "586- -115 6888 -4-4;bricks", "586- -115 588- -4-5;bricks",
    "586- -115 6-78 -5-4;bricks", "586- -115 5-7- -5-5;bricks", "586- 1112 788- -4-5;bricks", "586- 1112 7-7- -5-5;bricks",
    "5831 -4-7 6-58 -5-4;bricks", "586- -4-5 6-58 -5-4;bricks", "6868 88-7 1121 -4-4;bricks", "586- -4-5 586- -4-5;bricks",
    "586- 1112 8888 -4-4;bricks", "5831 -4-7 586- -4-5;bricks", "6868 -4-4 5--- -C-B;bricks", "586- -4-5 5--- -C-B;bricks",
    "5831 -4-7 5-5- -5-5;bricks", "4343 5353 2121 8888", "43 5- 86 66;bricks", "43 53 68 66;bricks", "66 -4 5- 86;bricks", "43 21 -7 68;bricks",
    "43 5- -5 68;bricks", "43 53 53 68;bricks", "5-L-K-E- -L-L-O-O K-H-5-L- -5-K-E-E;bricks", "5-L-L-K- -L-K-5-O H-5-O-K- -H-E-E-H;bricks",
    "5-L-L-K- -L-K-5-O H-5-O--- -H-E-H-E;bricks", "5-L-L-K- -L---5-O 5-O-L-K- -E-E-E-H;bricks", "5-L-L-K- -L---5-O 5-O-L--- -E-E-H-E;bricks",
    "5-L-L-K- -L---5-O L-O-K-5- -E-E-H-E;bricks", "5-L-L-K- -L---5-O L-O---5- -E-H-E-E;bricks", "5-L-L-K- -L---5-O 5-O-K-H- -E-E-H-H;bricks",
    "5-L-L-K- -L---5-O 5-O---H- -E-H-E-H;bricks", "5-L-L-K- ---H-5-O O-L-O-L- -E-E-E-E;bricks", "5-L-K-E- -L-L-O-O H-H-5--- -5-K-H-E;bricks",
    "5-L-L--- -L-L-O-L 5-L-L--- -E-E-H-E;bricks", "5-L-L--- -L-L-O-L 5---5--- -H-E-H-E;bricks", "5-L-L--- -L-L-O-L 5---H-H- -H-E-H-H;bricks",
    "5-L-L--- -L-L-O-L --5-L-L- -E-E-E-H;bricks", "5-L-L--- -L-L-O-L --5---5- -E-H-E-H;bricks", "5-L-O-K- -L-L-L-O E-E-E-E- -5-L-L-K;bricks",
    "5-L-L-K- -L---5-O L-O-L-L- -E-E-E-E;bricks", "5-L-L--- -L-L-O-L H-5---H- -H-H-E-H;bricks", "5-L-L--- -L-L-O-L H-H-H-H- -H-H-H-H;bricks",
    "5-L-K-E- -L-L-O-O H-H-H-H- -5-K-H-H;bricks", "5-L-L--- ---5-O-L O-L-L-L- -E-E-E-E;bricks", "5-L-L--- ---5-O-L O-L---5- -E-H-E-E;bricks",
    "5-K-5-K- -L-O-L-O 5-L-L--- -E-E-H-E;bricks", "5-K-5-K- -L-O-L-O L-L---5- -E-H-E-E;bricks", "5-K-5-K- -L-O-L-O 5-L---H- -E-H-E-H;bricks",
    "5-K-5-K- -L-O-L-O 5---5--- -H-E-H-E;bricks", "5-K-5-K- -L-O-L-O L---H-5- -H-E-H-E;bricks", "5-L-K-E- -E-E-H-H O-O-O-O- -5-K-H-H;bricks",
    "5-L-5-E- -E-5-5-H O-O-5-5- -5-5-5-K;bricks", "5-L-5-E- -E-5-5-O 5-O-O-5- -5-E-5-H;bricks", "5-L-5-E- -L-5-5-O L-5-5-L- -5-H-5-E;bricks",
    "5-L-5-E- -L-5-5-O L-5-O-5- -5-E-5-E;bricks", "5-L-5-E- -L-5-5-O 5-5-O-H- -5-E-5-H;bricks", "5-O-5-E- -E-5-5-H O-O-5-5- -5-5-H-H;bricks",
    "5-L-L-K- -L-L-L-O E-E-E-E- -L-L-L-L;bricks", "5-O-5-E- -E-5-5-O 5-O-5-L- -5-5-E-H;bricks", "5-L-K-H- -L-L-O-O L-L-L-L- -E-E-E-E;bricks",
    "5-L-K-H- -L-L-O-O 5-L-L--- -E-E-H-E;bricks", "5-L-K-H- -L-L-O-O L-L---5- -E-H-E-E;bricks", "5-L-K-H- -L-L-O-O 5-L-K-H- -E-E-H-H;bricks",
    "5-L-K-H- -L-L-O-O 5-L---H- -E-H-E-H;bricks", "5-L-K-H- -L-L-O-O L---5-L- -H-E-E-E;bricks", "5-L-K-H- -L-L-O-O 5-K-5-K- -E-H-E-H;bricks",
    "5-L-K-H- -L-L-O-O 5-K-5--- -E-H-H-E;bricks", "5-L-K-H- -L-L-O-O 5---5-K- -H-E-E-H;bricks", "5-L-L-K- -L---5-O E-H-E-E- -L-L-L-L;bricks",
    "5-L-K-H- -L-L-O-O 5---5--- -H-E-H-E;bricks", "5-L-K-H- -L-L-O-O L-K-H-5- -E-H-H-E;bricks", "5-L-K-H- -L-L-O-O --5-L-L- -E-E-E-H;bricks",
    "5-L-K-H- -L-L-O-O --5-K-5- -E-E-H-H;bricks", "5-L-K-H- -L-L-O-O --5---5- -E-H-E-H;bricks", "5-L---H- -L-O-L-O L-L-L-L- -E-E-E-E;bricks",
    "5-L---H- -L-O-L-O 5-L-L--- -E-E-H-E;bricks", "5-L---H- -L-O-L-O L-L---5- -E-H-E-E;bricks", "5-L---H- -L-O-L-O 5-L---H- -E-H-E-H;bricks",
    "5-L---H- -L-O-L-O L---5-L- -H-E-E-E;bricks", "5-L-L-K- ---H-5-O H-E-H-E- -L-L-L-L;bricks", "5-L---H- -L-O-L-O 5-K-5--- -E-H-H-E;bricks",
    "5-L---H- -L-O-L-O 5---5-K- -H-E-E-H;bricks", "5-L---H- -L-O-L-O 5---5--- -H-E-H-E;bricks", "5-L---H- -L-O-L-O L---H-5- -H-E-H-E;bricks",
    "5-L---H- -L-O-L-O 5---H-H- -H-E-H-H;bricks", "5-L---H- -L-O-L-O --5-L-L- -E-E-E-H;bricks", "5-L---H- -L-O-L-O K-5---5- -H-H-E-E;bricks",
    "5-L---H- -L-O-L-O H-5---H- -H-H-E-H;bricks", "5-L-L--- -L-L-O-L E-E-E-E- -L-L-L-L;bricks", "5-L---H- -L-O-L-O H-H-5--- -H-H-H-E;bricks",
    "5-L---H- -L-O-L-O --H-H-5- -E-H-H-H;bricks", "5-L---H- -L-O-L-O H-H-H-H- -H-H-H-H;bricks", "5-L-O-5- -L-L-5-5 5-E-5-H- -5-H-5-H;bricks",
    "5-L-O-5- -L-L-5-5 H-5-5-H- -5-E-5-H;bricks", "5-L-O-5- -E-E-5-5 5-O-5-L- -5-H-5-H;bricks", "5-L-O-5- -E-E-5-5 5-O-O-5- -5-E-5-H;bricks",
    "5-L-O-5- -L-E-5-5 L-5-5-L- -5-H-5-E;bricks", "5-L-O-5- -L-E-5-5 L-5-O-5- -5-E-5-E;bricks", "5-L-O-5- -L-E-5-5 5-5-O-H- -5-E-5-H;bricks",
    "5-L-O-5- ---5-5-5 O-E-5-5- -5-H-5-E;bricks", "5-L-5-H- -E-5-5-H 5-L-E-5- -5-5-O-O;bricks", "5-L-5-H- -E-5-5-H 5-K-5-5- -5-5-L-O;bricks",
    "5-L-5-H- -E-5-5-H O-5-E-5- -5-5-O-L;bricks", "5-L-5-H- -E-5-5-H O-H-5-5- -5-5-L-L;bricks", "5-L-5-H- -E-5-5-O 5-5-E-E- -5-5-O-O;bricks",
    "5-L-5-O- -E-5-E-5 5-H-5-H- -5-5-L-O;bricks", "5-O-5-O- -E-5-E-5 E-5-E-5- -O-5-O-5;bricks", "5-O-5-O- -E-5-E-5 5-5-E-H- -L-5-O-5;bricks",
    "5-O-L-K- -L---5-O E-H-E-E- -5-L-L--;bricks", "5-O-5-O- -E-H-5-5 5-5-E-E- -L-5-O-5;bricks", "5-L-5-H- -E-5-5-H 5-L-L-5- -H-5-5-O;bricks",
    "5-L-5-H- -E-5-5-H O-5-L-5- -H-5-5-L;bricks", "5-L-5-H- -E-5-5-H O-O-5-5- -E-5-5-L;bricks", "5-L-5-H- -E-5-5-O 5-5-L-E- -H-5-5-O;bricks",
    "5-L-5-H- -E-5-5-O 5-5-K-5- -H-5-5-L;bricks", "5-L-5-H- -E-5-5-O 5-O-5-E- -E-5-5-O;bricks", "5-L-5-O- -E-5-E-5 L-5-L-5- -H-5-5-L;bricks",
    "5-L-5-O- -E-5-E-5 5-5-L-H- -H-5-5-O;bricks", "5-L-5-O- -E-H-5-5 5-5-L-E- -H-5-5-O;bricks", "5-L-O-K- -E-E-E-H 5-L-L--- -5-L-O-K;bricks",
    "5-L-5-E- -E-5-5-H 5-L-L-5- -5-L-5-O;bricks", "5-L-5-O- -L-H-5-5 E-5-H-5- -H-5-5-L;bricks", "5-L-5-H- -E-5-5-O 5-5-L-L- -H-H-5-5;bricks",
    "5-L-5-H- -E-5-5-O 5-O-5-L- -E-H-5-5;bricks", "5-L-5-O- -L-5-L-5 5-5-L-H- -E-E-5-5;bricks", "5-L-5-O- -E-5-E-5 5-O-5-O- -E-H-5-5;bricks",
    "5-L-5-O- -L-O-5-5 5-E-5-H- -E-H-5-5;bricks", "5-L-5-O- -E-H-5-5 5-O-5-L- -E-H-5-5;bricks", "5-L-L-5- -L-L-5-5 E-E-5-5- -O-H-5-5;bricks",
    "5-L-L-5- -L-L-5-5 5-E-5-H- -L-H-5-5;bricks", "5-L-5-E- -E-5-5-H 5-K-5-5- -5-O-5-O;bricks", "5-L-L-5- -L-L-5-5 H-5-5-H- -L-E-5-5;bricks",
    "5-L-L-5- -E-E-5-5 5-5-L-L- -O-H-5-5;bricks", "5-L-L-5- -E-E-5-5 5-O-5-L- -L-H-5-5;bricks", "5-L-L-5- -E-E-5-5 5-O-O-5- -L-E-5-5;bricks",
    "5-L-L-5- -L-E-5-5 E-5-5-L- -O-H-5-5;bricks", "5-L-L-5- ---5-5-5 H-E-5-5- -O-H-5-5;bricks", "5-L-E-5- -E-5-5-L 5-O-5-L- -L-H-5-5;bricks",
    "5-L-L-5- -L-L-5-5 H-5-5-O- -5-E-E-5;bricks", "5-L-L-5- ---5-5-5 O-E-5-5- -5-H-H-5;bricks", "5-L-5-E- -E-5-5-H O-5-L-5- -5-L-5-L;bricks",
    "5-O-E-5- -E-5-5-L 5-O-H-5- -5-5-5--;bricks", "5-L-5-E- -E-5-5-H O-H-5-5- -5-O-5-L;bricks", "5-L-5-E- -E-5-5-O 5-5-L-E- -5-L-5-O;bricks",
    "5-L-5-E- -E-5-5-O 5-5-K-5- -5-L-5-L;bricks", "5-L-5-E- -E-5-5-O 5-H-5-E- -5-O-5-O;bricks", "5-L-5-E- -E-5-5-O 5-H-H-5- -5-O-5-L;bricks",
    "5-L-5-E- -L-5-5-O E-5-5-E- -5-L-5-O;bricks", "5-L-5-E- -L-5-5-O E-5-H-5- -5-L-5-L;bricks", "5-L-O-K- -E-E-E-H L-L-K-5- -5-L-O-K;bricks",
    "5-L-5-L- -E-5-E-5 L-5-L-5- -5-L-5-L;bricks", "5-L-5-L- -E-5-E-5 5-5-L-H- -5-L-5-O;bricks", "5-L-5-L- -E-5-E-5 5-H-5-H- -5-O-5-O;bricks",
    "5-L-5-E- -E-5-5-O 5-5-L-L- -5-K-5-5;bricks", "5-O-5-E- -E-5-5-H 5-L-L-5- -L-L-5-5;bricks", "5-O-5-E- -E-5-5-H 5---5-5- -O-L-5-5;bricks",
    "5-O-5-E- -E-5-5-H H-H-5-5- -O-O-5-5;bricks", "5-O-5-E- -E-5-5-O 5-5-L-E- -L-L-5-5;bricks", "5-O-5-E- -E-5-5-H O-5-L-5- -5-L-H-5;bricks",
    "5-O-5-E- -E-5-5-H O-H-5-5- -5-O-H-5;bricks", "5-L-O-K- -E-E-E-H L-L---5- -5-O-L-K;bricks", "5-O-5-E- -E-5-5-O 5-5-L-L- -5-L-E-5;bricks",
    "5-O-5-E- -E-5-5-O 5-5---5- -5-O-E-5;bricks", "5-L-K-H- -L-L-O-O E-E-E-E- -L-L-L-L;bricks", "5-L---H- -L-O-L-O E-E-E-E- -L-L-L-L;bricks",
    "5-L-O-5- -E-E-5-5 5-5-L-E- -5-L-5-O;bricks", "5-L-O-5- -E-E-5-5 5-H-5-E- -5-O-5-O;bricks", "5-L-O-5- -L-E-5-5 E-5-5-E- -5-L-5-O;bricks",
    "5-L-O-5- -L-E-5-5 E-5-H-5- -5-L-5-L;bricks", "5-O-O-5- -E-E-5-5 5-5-L-E- -L-L-5-5;bricks", "5-O-O-5- -E-E-5-5 5-H-5-E- -L-O-5-5;bricks",
    "5-L-O-K- -E-E-E-H 5-K-5--- -5-O-O-K;bricks", "5-O-O-5- -E-E-5-5 5-5---5- -5-O-E-5;bricks", "5-O-O-5- -L-E-5-5 E-5-5-L- -5-L-E-5;bricks",
    "5-O-O-5- -L-E-5-5 E-5-H-5- -5-L-H-5;bricks", "5-O-O-5- ---5-5-5 H-E-5-5- -5-L-E-5;bricks", "5-L-O-K- -E-E-E-H L-K-H-5- -5-O-O-K;bricks",
    "5-L-L-K- -L-L-L-O L-L-L-L- -E-E-E-E;bricks", "5-L-L-K- -L-L-L-O 5-L-L-K- -E-E-E-H;bricks", "5-L-L-K- -L-L-L-O 5-L-L--- -E-E-H-E;bricks",
    "5-L-L-K- -L-L-L-O 5-L-K-H- -E-E-H-H;bricks", "5-L-L-K- -L-L-L-O 5-L---H- -E-H-E-H;bricks", "5-L-L-K- -L-L-L-O L-K-5-L- -E-H-E-E;bricks",
    "5-L-K-E- -E-E-5-K O-O-O-L- -5-K-H-H;bricks", "5-L-L-K- -L-L-L-O L---5-L- -H-E-E-E;bricks", "5-L-L-K- -L-L-L-O 5-K-5-K- -E-H-E-H;bricks",
    "5-L-L-K- -L-L-L-O 5-K-5--- -E-H-H-E;bricks", "5-L-L-K- -L-L-L-O 5---5-K- -H-E-E-H;bricks", "5-L-L-K- -L-L-L-O 5---5--- -H-E-H-E;bricks",
    "5-L-L-K- -L-L-L-O L-K-H-5- -E-H-H-E;bricks", "5-L-L-K- -L-L-L-O L---H-5- -H-E-H-E;bricks", "5-L-L-K- -L-L-L-O 5-K-H-H- -E-H-H-H;bricks",
    "5-L-L-K- -L-L-L-O 5---H-H- -H-E-H-H;bricks", "5-L-L-K- -L-L-L-O K-5-L-L- -H-E-E-E;bricks", "5-L-K-E- -L-L-O-O K-5---5- -5-K-E-E;bricks",
    "5-L-L-K- -L-L-L-O --5-L-L- -E-E-E-H;bricks", "5-L-L-K- -L-L-L-O H-5-L-K- -H-E-E-H;bricks", "5-L-L-K- -L-L-L-O H-5-L--- -H-E-H-E;bricks",
    "5-L-L-K- -L-L-L-O K-5-K-5- -H-E-H-E;bricks", "5-L-L-K- -L-L-L-O K-5---5- -H-H-E-E;bricks", "5-L-L-K- -L-L-L-O --5-K-5- -E-E-H-H;bricks",
    "5-L-L-K- -L-L-L-O --5---5- -E-H-E-H;bricks", "5-L-L-K- -L-L-L-O H-5-K-H- -H-E-H-H;bricks", "5-L-L-K- -L-L-L-O H-5---H- -H-H-E-H;bricks",
    "5-L-L-K- -L-L-L-O K-H-5-L- -H-H-E-E;bricks", "5-L-K-E- -L-L-O-O H-5---H- -5-K-E-H;bricks", "5-L-L-K- -L-L-L-O --H-5-L- -E-H-E-H;bricks",
    "5-L-L-K- -L-L-L-O H-H-5--- -H-H-H-E;bricks", "5-L-L-K- -L-L-L-O --H-H-5- -E-H-H-H;bricks", "5-L-L-K- -L-L-L-O H-H-H-H- -H-H-H-H;bricks",
    "5-L-L-K- -L-K-5-O L-L-O-L- -E-E-E-E;bricks", "5-L-L-K- -L-K-5-O 5-L-O-K- -E-E-E-H;bricks", "5-L-L-K- -L-K-5-O 5-L-O--- -E-E-H-E;bricks",
    "5-L-L-K- -L-K-5-O K-5-O-L- -H-E-E-E;bricks", "5-L-L-K- -L-K-5-O --5-O-L- -E-E-E-H;bricks")

  def toAbsolute(args: String, rows: Int =22, cols:Int = 22, shiftUp: Int =0, shiftLeft: Int = 0) = {
    val specs = args.split(";")
    val matrixLines = Matrix.toValidMatrixLines(specs.head).get
    val tileSpec = if (specs.length > 1) specs(1) else "checker"
    val tileType = TileType(tileSpec)
    val checker = tileType.toChecker(matrixLines)
    val shifted = Matrix.shift(checker, shiftUp).map(Matrix.shiftChars(_, shiftLeft))
    val extended = extend(shifted, rows, cols)
    val relative = extended.map(_.map(charToRelativeTuples).toArray)
    Matrix.toAbsolute(relative)
  }
}
