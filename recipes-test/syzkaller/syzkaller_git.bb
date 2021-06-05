SUMMARY = ""
DESCRIPTION = ""
HOMEPAGE = ""
BUGTRACKER = ""
SECTION = "base"

inherit go-mod

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=5335066555b14d832335aa4660d6c376"

GO_IMPORT = "github.com/google/syzkaller"

SRC_URI = "git://${GO_IMPORT};protocol=https;destsuffix=${BPN}-${PV}/src/${GO_IMPORT}"

SRCREV = "77e2b66864e69c17416614228723a1ebd3581ddc"

GO_LINKMODE_append += "-X github.com/google/syzkaller/prog.GitRevision=${SRCREV}"

# Work around a "--set-interpreter" bug in patchelf that corrupts the binary
# https://github.com/NixOS/patchelf/pull/243
SSTATEPOSTUNPACKFUNCS_remove = "uninative_changeinterp"

BBCLASSEXTEND += "native"
