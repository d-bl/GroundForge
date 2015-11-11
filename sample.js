var sample = {
  "nodes":[

    // Thread diagram.

    // "startOf" is used as a CSS-class to paint threads,
    // they should match "thread" properties of links, no spaces.
    {"startOf":"thread1"},{"startOf":"thread2"},{"startOf":"thread3"},{"startOf":"thread4"},
    {"title":"twist"},{"title":"twist"},{"title":"cross"},{"title":"twist"},{"title":"twist"},{"title":"cross"},
    {bobbin:true},{bobbin:true},{bobbin:true},{bobbin:true},

    // pair diagram

    {"title":"pair 1"},{"title":"pair 2"},{"title":"pair 3"},{"title":"pair 4"},
    {"title":"tc"},{"title":"tc"},{"title":"tctc"},
    {"title":"tc"},{"title":"tc"},
    {"title":"tctc"},{"title":"tc"},{"title":"tc"},
    {bobbin:true},{bobbin:true},{bobbin:true},{bobbin:true},

    // pins used in thread diagram
    {pin:true},{pin:true}],

  "links":[
    // border links keep the bobbins and start pins in proper order
    // thread numbers should match "startOf" defined for nodes
    // "start" and "end" properties should match IDs of markers created by the script
    // white markers emulate the over-under effect in thread diagrams

    // thread diagram

    /*  0 */ {"source": 0,"target": 1,"border":true},
    /*  1 */ {"source": 1,"target": 2,"border":true},
    /*  2 */ {"source": 2,"target": 3,"border":true},
    /*  3 */ {"source": 0,"target": 4,"thread":1,"start":"thread","end":"white"},
    /*  4 */ {"source": 1,"target": 4,"thread":2,"start":"thread"},
    /*  5 */ {"source": 2,"target": 5,"thread":3,"start":"thread","end":"white"},
    /*  6 */ {"source": 3,"target": 5,"thread":4,"start":"thread"},
    /*  7 */ {"source": 4,"target": 7,"thread":2,"end":"white"},
    /*  8 */ {"source": 4,"target": 6,"thread":1,"start":"white"},
    /*  9 */ {"source": 5,"target": 6,"thread":4,"end":"white"},
    /* 10 */ {"source": 5,"target": 8,"thread":3,"start":"white"},
    /* 11 */ {"source": 6,"target": 7,"thread":4,"start":"white"},
    /* 12 */ {"source": 6,"target": 8,"thread":1,"end":"white"},
    /* 13 */ {"source": 7,"target": 9,"thread":2,"start":"white"},
    /* 14 */ {"source": 8,"target": 9,"thread":3,"end":"white"},
    /* 15 */ {"source": 7,"target":10,"thread":4},
    /* 16 */ {"source": 9,"target":11,"thread":3,"start":"white"},
    /* 17 */ {"source": 9,"target":12,"thread":2},
    /* 18 */ {"source": 8,"target":13,"thread":1,"start":"white"},
    /* 19 */ {"source":10,"target":11,"border":true},
    /* 20 */ {"source":11,"target":12,"border":true},
    /* 21 */ {"source":12,"target":13,"border":true},
    {"source":4,"target":30,"toPin":true},
    {"source":5,"target":30,"toPin":true},
    {"source":6,"target":30,"toPin":true},
    {"source":6,"target":31,"toPin":true},
    {"source":7,"target":31,"toPin":true},
    {"source":8,"target":31,"toPin":true},
    {"source":9,"target":31,"toPin":true},

    // pair diagram

    {"source":14,"target":15,"border":true},
    {"source":15,"target":16,"border":true},
    {"source":16,"target":17,"border":true},
    {"source":14,"target":18,"end":"green","start":"pair"},
    {"source":15,"target":18,"end":"green","start":"pair"},
    {"source":16,"target":19,"end":"green","start":"pair"},
    {"source":17,"target":19,"end":"green","start":"pair"},
    {"source":18,"target":20,"start":"green","end":"red"},
    {"source":18,"target":21,"start":"green","end":"green","text":"|"},
    {"source":19,"target":20,"start":"green","end":"red"},
    {"source":19,"target":22,"start":"green","end":"green","text":"|"},
    {"source":20,"target":21,"start":"red","end":"green"},
    {"source":20,"target":22,"start":"red","end":"green"},
    {"source":21,"target":24,"start":"green","end":"green","text":"|"},
    {"source":21,"target":23,"start":"green","end":"red"},
    {"source":22,"target":23,"start":"green","end":"red"},
    {"source":22,"target":25,"start":"green","end":"green","text":"|"},
    {"source":23,"target":24,"start":"red","end":"green"},
    {"source":23,"target":25,"start":"red","end":"green"},
    {"source":24,"target":26,"start":"green"},
    {"source":24,"target":27,"start":"green"},
    {"source":25,"target":28,"start":"green"},
    {"source":25,"target":29,"start":"green"},
    {"source":26,"target":27,"border":true},
    {"source":27,"target":28,"border":true},
    {"source":28,"target":29,"border":true},
  ]
};
