#
# SPDX-License-Identifier: MIT
#

from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import runCmd, bitbake, get_bb_var, get_bb_vars

class TestSyzkallerWR(OESelftestTestCase):
    def setUpSyzkallerConfig(self):
        syz_target_sysroot = get_bb_var('PKGD', 'syzkaller')
        syzkaller_native = get_bb_var('RECIPE_SYSROOT_NATIVE', 'syzkaller-native')
        self.logger.info("syz_target_sysroot %s" % syz_target_sysroot)

        self.syz_manager_bin = os.path.join(syzkaller_native, 'usr/bin/syz-manager')
        self.syzkaller_target = os.path.join(syz_target_sysroot, 'usr')
        self.logger.info("self.syzkaller_target %s" % self.syzkaller_target)
        self.syzkaller_workdir = os.path.join(self.topdir, 'syzkaller_workdir')
        self.syzkaller_cfg = os.path.join(self.syzkaller_workdir, 'wrl.cfg')

        bb_vars = get_bb_vars(['SYZ_FUZZTIME', 'SYZ_QEMU_MEM', 'SYZ_QEMU_CPUS'])
        self.syzkaller_fuzztime = bb_vars['SYZ_FUZZTIME'] or 3600
        self.logger.info("self.syzkaller_fuzztime %s" % self.syzkaller_fuzztime)
        self.syzkaller_qemu_mem = bb_vars['SYZ_QEMU_MEM'] or 1024
        self.logger.info("self.syzkaller_qemu_mem %s" % self.syzkaller_qemu_mem)
        self.syzkaller_qemu_cpus = bb_vars['SYZ_QEMU_CPUS'] or 2
        self.logger.info("self.syzkaller_qemu_cpus %s" % self.syzkaller_qemu_cpus)
        self.syzkaller_vms = self.nprocs // self.syzkaller_qemu_cpus or 1

        if not os.path.exists(self.syzkaller_workdir):
            os.mkdir(self.syzkaller_workdir)

        with open(self.syzkaller_cfg, 'w') as f:
            f.write(
"""
{
	"target": "linux/amd64",
	"http": "127.0.0.1:56741",
	"workdir": "%s",
	"kernel_obj": "%s",
	"kernel_src": "%s",
	"image": "%s",
	"syzkaller": "%s",
	"type": "qemu",
	"reproduce" : false,
	"vm": {
		"count": %s,
		"kernel": "%s",
		"cpu": %s,
		"mem": %s
	}
}
"""
% (self.syzkaller_workdir, self.kernel_objdir, self.kernel_src, self.rootfs, \
   self.syzkaller_target, self.syzkaller_vms, self.kernel, self.syzkaller_qemu_cpus,
   self.syzkaller_qemu_mem)
            )

    def setUpLocal(self):
        super(TestSyzkallerWR, self).setUpLocal()

        self.image = 'wrlinux-image-glibc-core'
        self.machine = 'intel-x86-64'
        self.fstypes = "ext4"

        bb_vars = get_bb_vars(['TOPDIR', 'DEPLOY_DIR_IMAGE', 'STAGING_KERNEL_DIR'])

        self.topdir = bb_vars['TOPDIR']
        self.deploy_dir_image = bb_vars['DEPLOY_DIR_IMAGE']
        self.kernel_src = bb_vars['STAGING_KERNEL_DIR']

        self.nprocs = os.cpu_count() or 1
        self.kernel = os.path.join(self.deploy_dir_image, 'bzImage')
        self.rootfs = os.path.join(self.deploy_dir_image, '%s-%s.ext4' % (self.image, self.machine))
        self.kernel_objdir = self.deploy_dir_image

        self.setUpSyzkallerConfig()

        self.write_config(
"""
MACHINE = "%s"
IMAGE_FSTYPES = "%s"
"""
% (self.machine, self.fstypes)
        )

        bitbake(self.image, output_log=self.logger)
        bitbake('syzkaller-native -c addto_recipe_sysroot', output_log=self.logger)
        bitbake('syzkaller', output_log=self.logger)

    def test_syzkaller_wr(self):
        runCmd([self.syz_manager_bin, '-config', self.syzkaller_cfg], timeout=self.syzkaller_fuzztime, output_log=self.logger, ignore_status=True, shell=False)