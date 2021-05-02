require linux-yocto-syzkaller.inc

SRC_URI += " \
    file://0001-lockdep-Allow-tuning-tracing-capacity-constants.patch \
    file://0001-mm-kmemleak-disable-early-logging-in-case-of-error.patch \
    file://0002-mm-kmemleak-make-the-tool-tolerant-to-struct-scan_ar.patch \
    file://0003-mm-kmemleak-simple-memory-allocation-pool-for-kmemle.patch \
    file://0004-mm-kmemleak-use-the-memory-pool-for-early-allocation.patch \
"
