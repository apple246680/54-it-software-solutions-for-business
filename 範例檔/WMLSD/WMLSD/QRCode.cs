using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WMLSD
{
    public partial class QRCode : Form
    {
        public QRCode(Bitmap bitmaap)
        {
            InitializeComponent();
            pictureBox1.Image = bitmaap;
        }
    }
}
