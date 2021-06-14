require linux-yocto-syzkaller.inc

SRC_URI += " \
    file://0001-lockdep-Allow-tuning-tracing-capacity-constants.patch \
    file://0001-mm-kmemleak-disable-early-logging-in-case-of-error.patch \
    file://0002-mm-kmemleak-make-the-tool-tolerant-to-struct-scan_ar.patch \
    file://0003-mm-kmemleak-simple-memory-allocation-pool-for-kmemle.patch \
    file://0004-mm-kmemleak-use-the-memory-pool-for-early-allocation.patch \
    file://0001-usb-gadget-add-raw-gadget-interface.patch \
    file://0002-usb-raw-gadget-Fix-copy_to-from_user-checks.patch \
    file://0003-usb-raw-gadget-fix-raw_event_queue_fetch-locking.patch \
    file://0004-usb-raw-gadget-fix-return-value-of-ep-read-ioctls.patch \
    file://0005-usb-raw-gadget-fix-gadget-endpoint-selection.patch \
    file://0006-usb-raw-gadget-support-stalling-halting-wedging-endp.patch \
    file://0007-usb-raw-gadget-fix-null-ptr-deref-when-reenabling-en.patch \
    file://0008-usb-raw-gadget-fix-memory-leak-in-gadget_setup.patch \
    file://0009-kcov-remote-coverage-support.patch \
    file://0010-kernel-kcov.c-fix-typos-in-kcov_remote_start-documen.patch \
    file://0011-kcov-cleanup-debug-messages.patch \
    file://0012-kcov-fix-potential-use-after-free-in-kcov_remote_sta.patch \
    file://0013-kcov-move-t-kcov-assignments-into-kcov_start-stop.patch \
    file://0014-kcov-move-t-kcov_sequence-assignment.patch \
    file://0015-kcov-use-t-kcov_mode-as-enabled-indicator.patch \
    file://0016-kcov-collect-coverage-from-interrupts.patch \
    file://0017-kcov-check-kcov_softirq-in-kcov_remote_stop.patch \
    file://0018-kcov-make-some-symbols-static.patch \
    file://0019-kernel-make-kcov_common_handle-consider-the-current-.patch \
    file://0020-usb-gadget-dummy_hcd-remove-useless-cast-for-driver..patch \
    file://0021-USB-dummy-hcd-Add-missing-annotation-for-set_link_st.patch \
    file://0022-USB-dummy-hcd-use-configurable-endpoint-naming-schem.patch \
    file://0024-USB-dummy-hcd-Fix-uninitialized-array-use-in-init.patch \
    file://0025-USB-Gadget-dummy-hcd-Fix-shift-out-of-bounds-bug.patch \
    file://0026-USB-gadget-dummy-hcd-Fix-errors-in-port-reset-handli.patch \
    file://0027-USB-gadget-dummy-hcd-remove-redundant-initialization.patch \
    file://0028-usb-gadget-dummy_hcd-fix-gpf-in-gadget_setup.patch \
"
