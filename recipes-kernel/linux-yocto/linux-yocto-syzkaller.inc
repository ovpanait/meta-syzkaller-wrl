FILESEXTRAPATHS_prepend := "${THISDIR}/linux-yocto:"

KERNEL_IMAGETYPES += "vmlinux"

SRC_URI += " \
    file://defconfig \
    file://syzkaller.scc \
"
