SUMMARY = ""
DESCRIPTION = ""
HOMEPAGE = ""
BUGTRACKER = ""
SECTION = "base"

inherit go-mod

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=5335066555b14d832335aa4660d6c376"

GO_IMPORT = "github.com/google/syzkaller"

SRC_URI = "git://${GO_IMPORT};protocol=https;destsuffix=${BPN}-${PV}/src/${GO_IMPORT} \
           file://0001-Makefile-Allow-GOHOSTFLAGS-and-GOTARGETFLAGS-to-be-o.patch;patchdir=src/${GO_IMPORT}"

SRCREV = "77e2b66864e69c17416614228723a1ebd3581ddc"

B = "${S}/src/${GO_IMPORT}/bin"

GO_LINKMODE_append += "-X ${GO_IMPORT}/prog.GitRevision=${SRCREV}"

# Work around a "--set-interpreter" bug in patchelf that corrupts the binary
# https://github.com/NixOS/patchelf/pull/243
python uninative_changeinterp () {
    return
}

export GOHOSTFLAGS="${GOBUILDFLAGS}"
export GOTARGETFLAGS="${GOBUILDFLAGS}"
export TARGETOS = '${GOOS}'
export TARGETARCH = '${GOARCH}'
export TARGETVMARCH = '${GOARCH}'

CGO_ENABLED = "0"

do_compile_class-native() {
    export HOSTOS="${GOHOSTOS}"
    export HOSTARCH="${GOHOSTARCH}"
    oe_runmake host
}

do_compile_class-target() {
    export HOSTOS="${GOOS}"
    export HOSTARCH="${GOARCH}"
    oe_runmake CC="${CC}" CFLAGS="${CFLAGS} ${LDFLAGS}" REV="${SRCREV}"
}

do_install() {
    SYZ_BINS_COMMON="\
        syz-manager \
        syz-runtest \
        syz-repro \
        syz-mutate \
        syz-prog2c \
        syz-db \
        syz-upgrade \
    "

    install -d ${D}${bindir}

    for i in ${SYZ_BINS_COMMON}; do
        install -m 0755 ${B}/${i} ${D}${bindir}
    done
}

do_install_append_class-target() {
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
