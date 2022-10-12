package com.eighteengray.procameralibrary.dataevent;


public class CameraConfigure {
    public static class Flash {
        private int flash;
        public int getFlash() {
            return flash;
        }
        public void setFlash(int flash)
        {
            this.flash = flash;
        }
    }

    public static class Scene {
        private String scene;

        public String getScene() {
            return scene;
        }

        public void setScene(String scene) {
            this.scene = scene;
        }
    }

    public static class Effect {
        private String effect;

        public String getEffect() {
            return effect;
        }

        public void setEffect(String effect) {
            this.effect = effect;
        }
    }

    public static class Ratio {
        private int ratio;
        public int getRatio() {
            return ratio;
        }
        public void setRatio(int ratio) {
            this.ratio = ratio;
        }
    }

    public static class DelayTime {
        private int delaytime;
        public int getDelaytime() {
            return delaytime;
        }
        public void setDelaytime(int delaytime) {
            this.delaytime = delaytime;
        }
    }
}
