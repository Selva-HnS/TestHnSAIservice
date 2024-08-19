package com.hns.acumen360.utils.common


/**
 * Created by Kamesh Kannan on 22-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
object CommonData {

    val imageStyle = "img{display: inline;height: auto;max-width: 100%;}"
    val tableStyle =
        "table {width: 100%; border: 1px solid #ACACAC; border-radius: 10px; border-spacing: 0; overflow-y: hidden; overflow-x: auto; display: block; border-collapse: collapse;}" +
                "thead { display: table-header-group; vertical-align: middle; border-color: inherit; border-collapse: separate; }" +
                "tbody { overflow-x: auto; white-space: nowrap; max-width: 95vw; }" +
                "tr { display: table-row; vertical-align: inherit; border-color: inherit; }" +
                "th, td { padding: 5px 4px 6px 4px; text-align: left; vertical-align: top; border-left: 1px solid #ddd;}" +
                "th {padding: 7px; background-color: #E4E4E4;} " +
                "td {padding: 7px; border-top: 1px solid #ACACAC;}" +
                "thead:first-child tr:first-child th:first-child, tbody:first-child tr:first-child td:first-child { border-radius: 4px 0 0 0; }" +
                "thead:last-child tr:last-child th:first-child, tbody:last-child tr:last-child td:first-child { border-radius: 0 0 0 4px;}"


    val styleTag = "<style> $imageStyle $tableStyle </style>"
    val bodyTag = "<body>$styleTag</body>"
    val cssStyle: String = "<html>$bodyTag</html>"

    val yourData1: String =
        "Certainly! Here is the detailed procedure for the removal and reinstallation of the Clutch Master Cylinder on the XUV 700, using the exact content from the manual including images:\n" +
                "\n" +
                "### Clutch Master Cylinder - Removal & Reinstallation (R&R)\n" +
                "\n" +
                "#### Tools Required:\n" +
                "\n" +
                "- Nose plier: 1\n" +
                "- Small flat screwdriver: 1\n" +
                "- L shape screwdriver: 1\n" +
                "- 12 mm & 14 mm Socket spanner: 1 each\n" +
                "\n" +
                "![Tools Required](https://dev.hnsonline.com/acumen360/xuv700clutch/0000414501.png)\n" +
                "\n" +
                "#### Pre-Removal:\n" +
                "- **Open the bonnet.**\n" +
                "- **Drain the brake & clutch fluid reservoir:** Use string to take out the brake fluid.\n" +
                "\n" +
                "#### Removal Procedure:\n" +
                "1. **Open the driver door.**\n" +
                "   ![Open Door](https://dev.hnsonline.com/acumen360/xuv700clutch/0000414502.png)\n" +
                "2. **Remove the driver side IP lower trim:** For detailed steps, refer to IP Driver Side Lower Trim With Switch Bank - R&R Section.\n" +
                "3. **Loosen and remove the clutch master cylinder to clutch pedal mounting bolts** using a 14 mm socket spanner.\n" +
                "   ![Remove Mounting Bolts](https://dev.hnsonline.com/acumen360/xuv700clutch/0000414518.png)\n" +
                "4. **Detach the clutch master cylinder link from the clutch pedal.**\n" +
                "5. **Remove the engine top NVH encapsulation cover.**\n" +
                "6. **Detach the low pressure hose from the clutch master cylinder.**\n" +
                "7. **Lift the high pressure pipe locking clip with a suitable screwdriver and detach.**\n" +
                "   ![Detach High Pressure Pipe](https://dev.hnsonline.com/acumen360/xuv700clutch/0000414489.png)\n" +
                "8. **Remove the clutch master cylinder separately.**\n" +
                "   ![Remove CMC](https://dev.hnsonline.com/acumen360/xuv700clutch/0000414520.png)\n" +
                "\n" +
                "#### Inspection:\n" +
                "- **Inspect for fluid leakage.**\n" +
                "- **Check the push rod lock for damage.**\n" +
                "\n" +
                "#### Installation:\n" +
                "1. **Reverse the removal steps** to install the clutch master cylinder.\n" +
                "2. **Ensure all connections are secure.**\n" +
                "3. **Refill and bleed the system:** This is crucial to ensure no air is trapped in the system.\n" +
                "\n" +
                "#### Final Steps:\n" +
                "- **Close the bonnet:** Ensure all tools and parts are accounted for.\n" +
                "- **Test the clutch operation** to ensure there are no leaks or operational issues.\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "Following this procedure closely will help ensure proper maintenance and function of the clutch master cylinder." +
                "\n" +
                "| Name           |Name 1         |Name 2         | Age | Occupation    |\n" +
                "|----------------|-----|---------------|\n" +
                "| John Doe       |John Doe       |John Doe       | 35  | Engineer      |\n" +
                "| Jane Smith     |Jane Smith     |Jane Smith     | 28  | Designer      |\n" +
                "| Michael Johnson|Michael Johnson|Michael Johnson| 42  | Developer     |\n" +
                "| Sarah Williams |Sarah Williams |Sarah Williams | 31  | Scientist     |\n" +
                "| S Williams |S Williams |S Williams | 31  |      |\n" +
                "| Williams |Williams |Williams |   | Scientist     |\n" +
                "| Sarah  |Sarah  |Sarah  | 33  |      |\n"


    val ADAS_BRAKE_ASSIST_URL = "https://www.youtube.com/watch?v=ITaUyMyqZXo"
    val PENTALINK_SUSPENSION_WITH_FDD_TECH_URL = "https://www.youtube.com/watch?v=ZKvTEMXt_w8"

    val ADAS_BRAKE_ASSIST = "ADAS Brake Assist"
    val PENTALINK_SUSPENSION_WITH_FDD_TECH = "Pentalink Suspension with FDD Tech"

    val CLUTCH_COVER_AND_DISC_INSTALLATION = "installation"
    val CLUTCH_COVER_AND_REMOVAL = "removal"
}