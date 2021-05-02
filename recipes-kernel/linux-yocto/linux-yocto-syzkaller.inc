FILESEXTRAPATHS_prepend := "${THISDIR}/linux-yocto:"

KERNEL_IMAGETYPES += "vmlinux"

SRC_URI += " \
    file://syzkaller.scc \
"
