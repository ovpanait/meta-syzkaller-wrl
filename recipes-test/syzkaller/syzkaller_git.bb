DESCRIPTION = "syzkaller is an unsupervised coverage-guided kernel fuzzer"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=5335066555b14d832335aa4660d6c376"

inherit go-mod

GO_IMPORT = "github.com/google/syzkaller"

SRC_URI = "git://${GO_IMPORT};protocol=https;destsuffix=${BPN}-${PV}/src/${GO_IMPORT};branch=master \
           file://0001-sys-targets-targets.go-allow-hardcoded-cross-compile.patch;patchdir=src/${GO_IMPORT} \
           "

SRCREV = "d0830353e30438120e98eb8b8c4c176095093fad"

B = "${S}/src/${GO_IMPORT}/bin"

GO_EXTRA_LDFLAGS += ' -X ${GO_IMPORT}/prog.GitRevision=0'

export GOHOSTFLAGS="${GO_LINKSHARED} ${GOBUILDFLAGS}"
export GOTARGETFLAGS="${GO_LINKSHARED} ${GOBUILDFLAGS}"
export TARGETOS = '${GOOS}'
export TARGETARCH = '${GOARCH}'
export TARGETVMARCH = '${GOARCH}'

CGO_ENABLED = "0"

do_compile:class-native() {
    export HOSTOS="${GOHOSTOS}"
    export HOSTARCH="${GOHOSTARCH}"
    oe_runmake host
}

do_compile:class-target() {
    export HOSTOS="${GOOS}"
    export HOSTARCH="${GOARCH}"
    export SYZ_CC_${TARGETOS}_${TARGETARCH}="${CC}"
    oe_runmake CC="${CC}" CFLAGS="${CFLAGS} ${LDFLAGS}" REV=0 target
}

do_install:class-native() {
    SYZ_BINS_NATIVE="\
        syz-manager \
        syz-runtest \
        syz-repro \
        syz-mutate \
        syz-prog2c \
        syz-db \
        syz-upgrade \
    "

    install -d ${D}${bindir}

    for i in ${SYZ_BINS_NATIVE}; do
        install -m 0755 ${B}/${i} ${D}${bindir}
    done
}

do_install:class-target() {
    SYZ_TARGET_DIR="${TARGETOS}_${TARGETARCH}"
    SYZ_BINS_TARGET=" \
        syz-fuzzer \
        syz-execprog \
        syz-stress \
        syz-executor \
    "

    install -d ${D}${bindir}/${SYZ_TARGET_DIR}

    for i in ${SYZ_BINS_TARGET}; do
        install -m 0755 ${B}/${SYZ_TARGET_DIR}/${i} ${D}${bindir}/${SYZ_TARGET_DIR}
    done
}

BBCLASSEXTEND += "native"
