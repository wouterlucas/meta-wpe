include westeros.inc

SUMMARY = "This receipe compiles the westeros compositor component"

SRC_URI += "file://0001-Use-intptr_t-to-avoid-precision-errors-on-aarch64.patch \
            file://0002-Set-exports-at-westeros-sysvinit.patch \
            file://0003-Add_VCX_flags_support.patch \
            file://0005-Dispatch-custom-queue-instead-flushing-display.patch \
"

PACKAGECONFIG ??= "incapp inctest increndergl incsbprotocol xdgv5"

PACKAGECONFIG_append = "${@bb.utils.contains("DISTRO_FEATURES", "x11", " x11", "", d)}"

PACKAGECONFIG[incapp] = "--enable-app=yes"
PACKAGECONFIG[inctest] = "--enable-test=yes"
PACKAGECONFIG[inctest] = "--enable-test=yes"
PACKAGECONFIG[increndergl] = "--enable-rendergl=yes"
PACKAGECONFIG[incsbprotocol] = "--enable-sbprotocol=yes"
PACKAGECONFIG[xdgv4] = "--enable-xdgv4=yes"
PACKAGECONFIG[xdgv5] = "--enable-xdgv5=yes"
PACKAGECONFIG[x11] = ",,freeglut"

WESTEROS ?= "${@bb.utils.contains("MACHINE_FEATURES", "vc4graphics", "", "westeros-soc", d)}"

S = "${WORKDIR}/git"

westeros-soc_hikey-32 = "westeros-soc-drm"

westeros-soc_dragonboard-410c-32 = "westeros-soc-drm"

westeros-soc_dragonboard-820c-32 = "westeros-soc-drm"

westeros-soc_poplar-32 = "westeros-soc-drm"

westeros-soc_imx8mqevk = "westeros-soc-drm"

DEPENDS += "\
           westeros-simplebuffer \
           westeros-simpleshell \
           ${WESTEROS} \
          "

RDEPENDS_${PN} = "xkeyboard-config"

inherit autotools pkgconfig systemd update-rc.d

SECURITY_CFLAGS_remove = "-fpie"
SECURITY_CFLAGS_remove = "-pie"

do_compile_prepend() {
   export SCANNER_TOOL=${STAGING_BINDIR_NATIVE}/wayland-scanner
   oe_runmake -C ${S}/protocol
}

INITSCRIPT_NAME = "westeros"
INITSCRIPT_PARAMS = "defaults"

#SYSTEMD_SERVICE_${PN} = "westeros.service"

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so"
INSANE_SKIP_${PN} += "dev-so"
